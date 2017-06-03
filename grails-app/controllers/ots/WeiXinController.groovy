package ots

import groovy.json.JsonSlurper
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.dao.DataIntegrityViolationException

class WeiXinController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	static {
		// to trigger the class load of WeiXinUtil
		println "WEIXIN_SRV_URL is: ${WeiXinUtil.WEIXIN_SRV_URL}"
	}
	
	// ===================================================================================
	// TO test 微信 without mobile phone, update buffer value and goto http://localhost:9090/ots/weiXin/list
    def index() {
		println "WeiXin index: ${new Date()}"
		
		// 验证消息的确来自正确的微信服务器, Token='OTSWeiXin'，仅检验一次，微信公众平台输入服务器地址URL, Token: OTSWeiXin,点击提交，
		// 成为微信开发者后，就不用再检验了 only check once，
		if ("GET" == request.method && params.nonce && params.timestamp && params.signature) {
			println "request: ${request}, method: ${request.method}"
			println "params: ${params}"
			// GET 校验URL
			if (checkSignature(params.nonce, params.timestamp, params.signature)) {
				render(params.echostr)
			}
			else {
				render("<font size='6'>Not passed Signature of echostr: signature=${params.signature}, echostr=${params.echostr}</font>")
			}
			
			return
		}
		else if ("POST" == request.method) {
			// 创建菜单如果还没有 moved to 管理员界面，管理员主动触发
			// if (!WeiXinUtil.MENUE_CREATED)
			//	createMenu()
				
			// POST 接收消息
			InputStream inputStream = request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			StringBuffer buffer = new StringBuffer()

			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;

			// println buffer
			
			def renderXMLStr = new StringWriter()
			
			// TODO - remove below codes in product env, below is only for debug locally
			if (!buffer) {
				// TODO - update below to trigger different actions and test through http://localhost:9090/ots/weiXin/list
				// String content = "q.a ";
				buffer = new StringBuffer("""
<xml>
	<ToUserName><![CDATA[gh_f09298fec741]]></ToUserName>
    <FromUserName><![CDATA[oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU]]></FromUserName><CreateTime>1423733337</CreateTime>
	<MsgType><![CDATA[event]]></MsgType><Event><![CDATA[CLICK]]></Event><EventKey><![CDATA[binding]]></EventKey>
	<Ticket><![CDATA[gQHo7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xLy1ValR0QkxsbmFma3k3bk9BV1NYAAIEBXLcVAMEAAAAAA==]]></Ticket>
</xml>
""");
			}
			
			if (!buffer) {
				renderXMLStr = "error, no post data got"
			}
			else {
				println "received XML : ${buffer.toString()}"
				
				XmlParser parser = new XmlParser()

				// Node recvXml = parser.parse(request.getInputStream())

				Node recvXml = parser.parseText(buffer.toString())
				// recvXml.
				// println "received XML as groovy.util.Node: ${recvXml.getDirectChildren()}"

				String xToUserName = recvXml.ToUserName[0].text()
				String xFromUserName = recvXml.FromUserName[0].text()
				String xCreateTime = recvXml.CreateTime[0].text()
				String xMsgType = recvXml.MsgType[0].text()
				//String xMsgId = recvXml.MsgId[0].text()
				
				// set session.user if 微信号已经绑定了高手云账号
				if (!session.user) {
					Student checkStu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkStu) {
						println "您的微信号已经绑定了高手云账号: ${checkStu.userName}, 设置session.user，该session.user作用范围不会包含新打开的页面"
						session.user = checkStu
					}
				}
				
				WeiXin wxUser = WeiXin.findByOpenId(xFromUserName)
				if (!wxUser) {
					wxUser = new WeiXin(openId:xFromUserName)
					if (wxUser.save()) {
						println "WeiXin user inserted into DB successfully"
					} else {
						println "WeiXin user inserted into DB failed"
					}
				}
				
				session.weixinOpenId = xFromUserName

				String x = recvXml.ContentX[0]?.text()
				// println Node.attributes().get("attr") or Node.attribute("attr")
				
				// =================================== 如果接收的是文本消息  ===================================
				if (xMsgType == "text") {
					String xContent = recvXml.Content[0].text()
					
					def c = xContent?.toLowerCase()?.trim()
					
					if (c?.startsWith("user ") || c?.startsWith("user.")) {
						// - 用户信息
						renderXMLStr = userActions(xToUserName, xFromUserName, c)
						
					} else if (c?.startsWith("kp ") || c?.startsWith("kp.")) {
						// - 查询知识点，以及其题目，比如：kp take off #3  (查询知识点为 take off，并且第三页)
						String[] commandItems = c.split("\\s+")
						renderXMLStr = kpActions(xToUserName, xFromUserName, commandItems)
						
					} else if (c?.startsWith("q.")) {  // no c?.startsWith("q ")
						renderXMLStr = questionActions(xToUserName, xFromUserName, c)
						
					} else if (wxUser?.latestMenuClick == "binding") {
						String[] items = c.split("\\s+")
						String userName = items[0]
						String password = null
						if (items.length == 1) {
							password = "NO_PASSWORD_PROVIDED"
						} else {
							password = items[1]
						}
						
						renderXMLStr = mClick_binding(xToUserName, xFromUserName, userName, password)
					} else if (wxUser?.latestMenuClick == "query_kp") {
						String[] commandItems = new String[2]
						commandItems[0] = "kp"
						commandItems[1] = c
						renderXMLStr = renderXMLStr = kpActions(xToUserName, xFromUserName, commandItems)
					}
					else if (WeiXinReply.findByKeyWords(c)) {
						String replyContent = WeiXinReply.findByKeyWords(c)?.replyContent
						renderXMLStr = reply(xToUserName, xFromUserName, replyContent)
					}
					else {
						// help info
						renderXMLStr = reply(xToUserName, xFromUserName, WeiXinUtil.HELP_INFO)
					}
				}
				// =================================== 如果接收的是二维码扫描  ===================================
				else if (xMsgType == "event") {
					String xEvent = recvXml.Event[0].text()
					if (xEvent == 'subscribe') {
						// 首次扫描会提示关注，如果用户关注了，进入该block
						renderXMLStr = reply(xToUserName, xFromUserName, WeiXinReply.findByKeyWords(WeiXinUtil.SubscribeKeyWords4WeiXinReply)?.replyContent)
					} else if (xEvent == 'SCAN'){
						// 推送视频文件
						renderXMLStr = scanCode(xToUserName, xFromUserName, recvXml.EventKey[0].text())
					} else if (xEvent == 'CLICK'){
						// 菜单点击事件
						renderXMLStr = menuClick(xToUserName, xFromUserName, recvXml.EventKey[0].text())						
					}
				}
				
				// clear wxUser?.latestMenuClick
				if (xMsgType == "event" && wxUser?.latestMenuClick) {
					println "The continue input is used for ${wxUser?.latestMenuClick}"
				} else {
					wxUser?.latestMenuClick = null
					wxUser.save()
				}
			}
			
			println "render XML String is:\n${renderXMLStr}"
			render renderXMLStr
		}
		else {
			println "\nWARNING!!! something wrong??? weiXin/index(): "
			println "request: ${request}, method: ${request.method}"
			println "params: ${params}"
		}
	}
	
	// scan qrCode action - 扫描二维码操作
	private String scanCode(xToUserName, xFromUserName, eventKey)
	{
		if (!eventKey) {
			return "Error: No valid QrCode"
		}
		
		session.weixinOpenId = xFromUserName
		
		if (Util.isValidQrCode(Question.class.getName(), eventKey)[0]) {
			// 扫码弹出讲解视频
			return _scanCode4Question(xToUserName, xFromUserName, eventKey)
		}
		else if (Util.isValidQrCode(KnowledgePoint.class.getName(), eventKey)[0]) {
			// 扫码弹出讲解视频
			return _scanCode4KP(xToUserName, xFromUserName, eventKey)
		}
		else if (Util.isValidQrCode(Assignment.class.getName(), eventKey)[0]) {
			// 扫码生成一个练习Quiz
			return _scanCode4Assignment(xToUserName, xFromUserName, eventKey)
		}
		else if (Util.isValidQrCode(Chapter.class.getName(), eventKey)[0]) {
			// 每一讲最后（即单元小结），扫码出现一个诊断报告
			return _scanCode4Chapter(xToUserName, xFromUserName, eventKey)
		}
		else {
			println "invalid: ${eventKey}"
		}
	}
	
	private String _scanCode4Question(xToUserName, xFromUserName, eventKey)
	{
		Question ques = Question.findByQrCode(eventKey)
		
		def sw = new StringWriter()
		
		if (ques) {
			
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
				
				def articleCount = ques.knowledgePoints == null ? 0 : ques.knowledgePoints?.size()
				if (ques.knowledgePoints?.size() > 0) {
					articleCount = ques.knowledgePoints.size() + 1
				}
				
				ArticleCount (articleCount)
				Articles {
					if (ques.qrCodeVideoURL) {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[观看习题讲解视频]]>")}
							// Description {mkp.yieldUnescaped ("<![CDATA[视频讲解]]>")}
							PicUrl {mkp.yieldUnescaped ("<![CDATA[http://f.seals.qq.com/filestore/10024/c5/b3/2e/1000/pic/Post/201411/1417228212_1878515108.jpg]]>")}
							if (ques.qrCodeVideoURL?.startsWith("http")) {
								Url {mkp.yieldUnescaped ("<![CDATA[${ques.qrCodeVideoURL}]]>")}
							} else {
								Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL + '/video?vid='+ ques.qrCodeVideoURL}]]>")}
							}
						}
					}
					else {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[练习知识点]]>")}
						}
					}
					
					def kpIdListStr = ""
					ques?.knowledgePoints?.eachWithIndex { kp, i ->
						kpIdListStr += kp.id+"X"
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[ ${i+1}. 关联知识点: ${kp.name} ]]>")}
							// PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
							Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?sourceq=${ques.id}&openId=${xFromUserName}&kpList=${kp.id}]]>")}
						}
					}
					
					if (articleCount > 2) {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[ 练习上面列出的所有知识点 ]]>")}
							Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?sourceq=${ques.id}&openId=${xFromUserName}&kpList=${kpIdListStr}]]>")}
						}
					}
				}
			}
			
		}
		else {
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				Content {mkp.yieldUnescaped ("<![CDATA[没有找到该二维码关联的练习，也许您扫描的二维码没有与任何练习进行关联]]>")}
			}
		}
		
		return sw.toString()
	}
	
	private String _scanCode4KP(xToUserName, xFromUserName, eventKey)
	{
		KnowledgePoint kp = KnowledgePoint.findByQrCode(eventKey)
		
		def sw = new StringWriter()
		
		if (kp) {
			
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
				
				def articleCount = 2			
				ArticleCount (articleCount)
				Articles {
					if (kp.qrCodeVideoURL) {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[观看习题讲解视频]]>")}
							// Description {mkp.yieldUnescaped ("<![CDATA[视频讲解]]>")}
							PicUrl {mkp.yieldUnescaped ("<![CDATA[http://f.seals.qq.com/filestore/10024/c5/b3/2e/1000/pic/Post/201411/1417228212_1878515108.jpg]]>")}
							if (kp.qrCodeVideoURL?.startsWith("http")) {
								Url {mkp.yieldUnescaped ("<![CDATA[${kp.qrCodeVideoURL}]]>")}
							} else {
								Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL + '/video?vid='+ kp.qrCodeVideoURL}]]>")}
							}
						}
					}
					else {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[练习知识点]]>")}
						}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ 知识点: ${kp.name} ]]>")}
						//PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?openId=${xFromUserName}&kpList=${kp.id}]]>")}
					}					
				}
			}
			
		}
		else {
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				Content {mkp.yieldUnescaped ("<![CDATA[没有找到该二维码关联的知识点，也许您扫描的二维码没有与任何知识点进行关联]]>")}
			}
		}
		
		return sw.toString()
	}
	
	// 扫描作业二维码，redirect 到  quiz/continuePractice 做练习，练习结果界面需要针对手机做些调整
	private String _scanCode4Assignment(xToUserName, xFromUserName, eventKey)
	{
		Assignment assi = Assignment.findByQrCode(eventKey)
		
		def sw = new StringWriter()
		
		if (assi) {
			
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
				
				def kps = [] as HashSet
				assi.templates.each {
					it.knowledgePoints.each { kp-> 
						kps << kp
					}
				}
				
				ArticleCount (2)
				Articles {
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[--- 智能训练本期知识点  ---]]>")} // 练习该作业涉及的所有知识点
					}
					
					// 作业不需要视频讲解，直接弹出链接，练习所有知识点即可.作业可能会有很多个知识点，不要分别列车所有知识点，否则知识点太多微信无法展示
					item {
						String assiNameShow = assi.name
						if (assiNameShow.startsWith("sg")) {
							String regex = "sg[\\d]*.[\\d]*";
							java.util.regex.Pattern pat = java.util.regex.Pattern.compile(regex);
							java.util.regex.Matcher matcher = pat.matcher(assiNameShow);
							if (matcher.find()) {
							  //println "found = " +matcher.start()+" - "+matcher.end();
							  assiNameShow = assiNameShow.substring(matcher.end());
							}
						}
						Title {mkp.yieldUnescaped ("<![CDATA[ * ${assiNameShow} ]]>")}
						
						/*def kpIdListStr = ""
						 kps.eachWithIndex { kp, i ->
							kpIdListStr += kp.id+"X"
						}*/
						// below Url will redirect to the virtual Assignment A_FOR_WX, that is is temp use
						// Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEB_HOME}/practice?openId=${xFromUserName}&kpList=${kpIdListStr}]]>")}
						 
						// <WEB_HOME>/quiz/continuePractice?stuId=14&aStatusId=6
						Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
						session.user = stu
						AssignmentStatus aStatus = AssignmentStatus.findByAssignmentAndStudent(assi, stu)
						if (!aStatus) {
							aStatus = new AssignmentStatus(student:stu, assignment:assi, status:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN ).save()
						}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEB_HOME}/quiz/continuePractice?openId=${xFromUserName}&stuId=${stu?.id}&aStatusId=${aStatus?.id}]]>")}
					}
										
				}
			}
			
		}
		else {
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				Content {mkp.yieldUnescaped ("<![CDATA[没有找到该二维码关联的作业，也许您扫描的二维码没有与任何作业进行关联]]>")}
			}
		}
		
		return sw.toString()
	}
	
	private String _scanCode4Chapter(xToUserName, xFromUserName, eventKey)
	{
		Chapter chapter = Chapter.findByQrCode(eventKey)
		EndUser userScanned = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
		Student stu = null;
		def errMessage = null;
		
		if (userScanned.role == "parents") {
			stu = Student.findByParents(userScanned.userName)
		}
		else if (userScanned instanceof Student){
			stu = userScanned
		}
		else {
			errMessage = "章节锦囊妙计，必须是学生或者家长扫描二维码"
		}
		
		println "stu: ${stu}"
		
		def sw = new StringWriter()
		
		if (chapter) {
			// publish 章节锦囊妙计 to the user who scanned qrCode
			
			def focusedKPsToSend = []
			def totalQuesNum = 0
			def coveredQuesNum = 0
			def correctQuesNum = 0
			def totalKPNum = 0
			def coveredKPNum = 0
			
			chapter.assignment.each { assi ->
				println  "assi : ${assi}"
				AssignmentStatus aStatus = AssignmentStatus.findByStudentAndAssignment(stu, assi)
				
				totalQuesNum += assi.totalAvailableQuestions
				totalKPNum += assi.totalKnowledgePoints
				
				if (aStatus) {
					if (aStatus.toBeFocusedKnowledge) {
						focusedKPsToSend << aStatus.toBeFocusedKnowledge
					}					
					
					coveredQuesNum += aStatus.finishedQuestions
					correctQuesNum += aStatus.correctQuestions
					coveredKPNum += aStatus.coveredKnowledgPoints
				}
				
			}
			
			String title = "Hi, ${stu.fullName}同学, 这是章节[ ${chapter.name}]的锦囊妙计"
			String c1 = "薄弱知识点: "+focusedKPsToSend.toString()
			String c2 = "总题目数:${totalQuesNum}, 已经完成题目数:${coveredQuesNum}, 正确题目数:${correctQuesNum}"
			String c3 = "总知识点数:${totalKPNum}, 已经完成知识点数:${coveredKPNum}"
			
			// 不要用模版发送, 用 news 发送，包含章节信息以及锦囊妙计，章节信息点击后进入一个页面
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
				
				def articleCount = 4
				
				ArticleCount (articleCount)
				Articles {
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ ${title} ]]>")}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ ${c1} ]]>")}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ ${c2} ]]>")}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ ${c3} ]]>")}
					}
					
					/*item {
						Title {mkp.yieldUnescaped ("<![CDATA[ 点击进入 *核心考点及典型真题 ]]>")}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEB_HOME}/chapter/show/${chapter.id}?weixinOpenId=${xFromUserName}]]>")}
					}*/
				}
			}
			
			// 用模板信息发送
			/*
			String v1 = "送给 ${stu.fullName} ${chapter.name} 的锦囊妙计" 
			String v2 = "薄弱知识点: "+focusedKPsToSend.toString() 
			v2 += " [总题目数:${totalQuesNum}, 已经完成题目数:${coveredQuesNum}, 正确题目数:${correctQuesNum}]"
			v2 += " [总知识点数:${totalKPNum}, 已经完成知识点数:${coveredKPNum}]"
			
			def sendData = """
{
   "touser":"${xFromUserName}",
   "template_id": "v2xtrRK7bSX-lxRAhH-tcnl1GUW857hycGHGmqASr0c",
   "topcolor":"#FF0000",
     "data":{
           "keyword1": {
               "value":"${v1}",
               "color":"#173177"
           },
           "keyword2":{
               "value":"${v2}",
               "color":"#173177"
           },
           "remark":{
               "value":"继续加油！",
               "color":"#173177"
           }
   }
}"""
			String token = WeiXinUtil.getTOKEN()
			println "sendData: ${sendData}"
			WeiXinUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${token}", sendData)*/
						
		}
		else {
			errMessage = "没有找到该二维码关联的章节锦囊妙计"
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				Content {mkp.yieldUnescaped ("<![CDATA[${errMessage}]]>")}
			}
		}
		
		return sw.toString()		
	}
	
	private menuClick(xToUserName, xFromUserName, eventKey)
	{
		def sw = new StringWriter()			
		
		//def c = EndUser.createCriteria()
		//int currentMaxEndUserID = c.get { projections { max('id') } } + 1
				
		// reply msgType=news - 首先处理富文本消息
		if (eventKey == "weak_kp") {			
			def results = []
			Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
			
			stu?.aStatus?.each {
				if (it.toBeFocusedKnowledge && !results.contains(it.toBeFocusedKnowledge)) {
					results << it.toBeFocusedKnowledge
				}
			}
			
			def respXML = new groovy.xml.MarkupBuilder(sw)
			
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))		
										
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")} // 富文本消息
				
				int row = 0
				def kpIdListStr = ""
				Articles {
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[ 您的薄弱知识，快来个性化智能训练吧 ]]>")}
					}
				
					results.each { kp->
						row++
						kpIdListStr += kp.id+"X"
						item {
							def kpName = kp.name.replaceAll("/", "")
							Title {mkp.yieldUnescaped ("<![CDATA[ ${row}: ${kpName} ]]>")}
							PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
							Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?openId=${xFromUserName}&kpList=${kp.id}]]>")}
						}
						
					}
							
					// Add one item more	
					if (row == 0) {
						def msg = null
						if (!stu) {
							msg = "您的微信账号尚未绑定高手云账号"
						} else {
							msg = "没有找到薄弱知识点"
						}
						
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[ ${msg} ]]>")}
						}
						row++
					} else if (row > 1) {
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[ 练习上面列出的所有知识点 ]]>")}
							PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
							Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?openId=${xFromUserName}&kpList=${kpIdListStr}]]>")}
						}
						row++
					}
				}
				
				ArticleCount (row + 1)	// Title 所在的 item也是一个count
			}
			
		}	
		// 对于有些需要 显示链接的 回复图文消息
		else if (eventKey == "new_wx_user") {
			def respXML = new groovy.xml.MarkupBuilder(sw)
			
			respXML.xml
			{
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
								
				MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
				
				int articleCount = 4
				ArticleCount {mkp.yieldUnescaped ("<![CDATA[${articleCount}]]>")}
				Articles {
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[--- 会员注册 ---]]>")}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[学生注册]]>")}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/new_wx_user?opflag=new_wx_stu&openId=${xFromUserName}]]>")} 
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[家长注册]]>")}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/new_wx_user?opflag=new_wx_parents&openId=${xFromUserName}]]>")}
					}
					
					item {
						Title {mkp.yieldUnescaped ("<![CDATA[亲子绑定]]>")}
						Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/new_wx_user?opflag=parents_stu_binding&openId=${xFromUserName}]]>")}
					}
					
					if (false) // WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB
					{// if enable this, please set articleCount = 5
						item {
							Title {mkp.yieldUnescaped ("<![CDATA[请求帮助]]>")}
							Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/new_wx_user?opflag=ask_for_help&openId=${xFromUserName}]]>")}
						}
					}
					
				}
			}
		}
		/*else if (eventKey == "parents_stu_link") {
			def respXML = new groovy.xml.MarkupBuilder(sw)

			respXML.xml
			{
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				
				EndUser currentLoginUser = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
				
				if (!currentLoginUser) {
					MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
					Content {mkp.yieldUnescaped ("<![CDATA[您的微信号还没有注册会员，请点击菜单‘注册会员’进行自动注册]]>")}
				} else {// don't work
					MsgType {mkp.yieldUnescaped ("<![CDATA[link]]>")}
					Title {mkp.yieldUnescaped ("<![CDATA[--家长学生账户关联--]]>")}
					Description {mkp.yieldUnescaped ("<![CDATA[parents student account link]]>")}
					Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/parents_stu_link/${currentLoginUser.id}]]>")}
				}
			}
		}
		else if (eventKey == "new_wx_user") {
			def respXML = new groovy.xml.MarkupBuilder(sw)

			respXML.xml
			{
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				
				EndUser currentLoginUser = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
				
				if (!currentLoginUser) {
					MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
					Content {mkp.yieldUnescaped ("<![CDATA[您的微信号还没有注册会员，请点击菜单‘注册会员’进行自动注册]]>")}
				} else {// don't work, 这是格式是接收消息格式，不是回复
					MsgType {mkp.yieldUnescaped ("<![CDATA[event]]>")}
					Event {mkp.yieldUnescaped ("<![CDATA[VIEW]]>")}
					EventKey {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/new_wx_user/${currentLoginUser.id}]]>")}
				}
			}
		}*/
		// 文本消息
		else {
			// all other reply msgType=text
			def respXML = new groovy.xml.MarkupBuilder(sw)
			respXML.xml {
				ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
				FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
				CreateTime ((int)(System.currentTimeMillis()/1000))
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				
				def forUserMsg = null
							
				/*if (eventKey == "binding") { // 取消单独 bind 菜单了，
					
					Student checkStu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkStu) {
						forUserMsg = "您的微信号已经绑定了高手云账号: ${checkStu.userName}"
					} else {
						forUserMsg = "请输入您需要绑定的高手云注册用户名和密码（用户名和密码用空格隔开）"
						
						WeiXin wxUser = WeiXin.findByOpenId(xFromUserName)
						if (wxUser) {
							wxUser.latestMenuClick = "binding"
							wxUser.save()
						} else {
							forUserMsg = "系统错误：WeiXin.findByOpenId(xFromUserName) returns null"
						}
						
					}
					
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
				}
				else if (eventKey == "unbinding") {	// 没有 unbind 菜单了，
					Student checkStu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkStu) {
						forUserMsg = "您的微信号已经绑定了高手云账号: ${checkStu.userName}，"
						checkStu.weixinOpenid = null
						if (checkStu.save()) {
							forUserMsg += "解除绑定成功"
						} else {
							forUserMsg += "解除绑定失败"
						}
					} else {
						forUserMsg = "您的微信账号没有绑定任何高手云账号，无需解除绑定"
					}
					
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
				}
				else if (eventKey == "new_stu_binding") {//  取消了注册学生会员菜单,改用页面了
					EndUser checkStu = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkStu) {
						forUserMsg = "您的微信号已经绑定了高手云账号: ${checkStu.userName}, role=${checkStu.role}"
						if (checkStu instanceof Student) {
							if (!checkStu.parents) {
								forUserMsg += " (还未绑定家长帐号)"
							}
							else {
								forUserMsg += ", parents=${checkStu.parents}"
							}
						}
					} else {
						def uname = "wx${currentMaxEndUserID}"
						Student stu = new Student(userName: uname, password: uname, fullName: uname, role: "student", email:"weixin@weixin.com", teacher:Teacher.findByUserName("te"))
						stu.weixinOpenid = xFromUserName
										
						if (stu.save(failOnError:true)) {
							forUserMsg = "为您开通高手云学生账号：${uname}，并且绑定微信账号成功"
						} else {
							forUserMsg = "为您开通高手云学生账号：${uname}，并且绑定微信账号失败, errors:"+stu.errors
						}
					}
					
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
				}
				else if (eventKey == 'new_parents_binding') { // 取消了家长绑定菜单，改用页面了
					EndUser checkStu = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkStu) {
						forUserMsg = "您的微信号已经绑定了高手云账号: ${checkStu.userName}, role=${checkStu.role}"
					} else {
						def uname = "pwx${currentMaxEndUserID}"
						EndUser parents = new EndUser(userName: uname, password: uname, fullName: uname, role: "parents", email:"weixin@weixin.com")
						parents.weixinOpenid = xFromUserName
										
						if (parents.save(failOnError:true)) {
							forUserMsg = "为您开通高手云家长账号：${uname}，并且绑定微信账号成功"
						} else {
							forUserMsg = "为您开通高手云家长账号：${uname}，并且绑定微信账号失败, erros:"+parents.errors
						}
					}
					
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
				} 
				else */
				if (eventKey == "check_binding") {
					EndUser checkUser = EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
					if (checkUser) {
						def role = ""
						if (checkUser instanceof Student) {
							role = "学生"
						} else if (checkUser.role == "parents") {
							role = "家长"
						}
						forUserMsg = "您的微信号已经绑定了高手云${role}账号: ${checkUser.userName}"
					} else {
						forUserMsg = "您的微信号没有绑定高手云账号"
					}
					
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
				}
				else if (eventKey == "query_kp") {
					forUserMsg = "请输入您需要查询的知识点"
					Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
					
					WeiXin wxUser = WeiXin.findByOpenId(xFromUserName)
					if (wxUser) {
						wxUser.latestMenuClick = "query_kp"
						wxUser.save()
					} else {
						forUserMsg = "系统错误：WeiXin.findByOpenId(xFromUserName) returns null"
					}
				} 
				else {
					Content {mkp.yieldUnescaped ("<![CDATA[ 不认识的 eventKey]]>")}
				}
			} // end of respXML.xml {
		}
				
		return sw.toString()
	}
	
	@Deprecated
	private String mClick_binding(xToUserName, xFromUserName, String userName, String password)
	{
		def sw = new StringWriter()
		
		def forUserMsg = null
		
		Student stu = Student.findByUserNameAndPassword(userName, password)
		if (stu) {
			if (!stu.weixinOpenid) {
				stu.weixinOpenid = xFromUserName
				if (stu.save()) {
					forUserMsg = "绑定高手云账号${userName} 成功"
				} else {
					forUserMsg = "绑定高手云账号${userName} 失败"
				}
			} else if (xFromUserName != stu.weixinOpenid) {
				forUserMsg = "${userName} 已经被别的微信号${stu.weixinOpenid}绑定过了"
			}
		} else {
			forUserMsg = "没有找到用户名为${userName}/${password}的高手云注册用户或者用户名密码错误"
		}
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			Content {mkp.yieldUnescaped ("<![CDATA[${forUserMsg}]]>")}
		}
		
		return sw.toString()
	}
		
	private String userActions(xToUserName, xFromUserName, String c)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			String[] items = c.split("\\s+")
						
			if (items.length == 1) {
				respContext = WeiXinUtil.HELP_INFO
			} 
			else {
				if (c.startsWith("user ")) {
					def userName = items[1]
					
					Student stu = Student.findByUserName(userName)
					if (stu) {
						respContext = "用户名：${stu.userName}：\n注册时间，${stu.dateCreated}，\n老师：${stu.teacher?.userName}"
					} else {
						respContext = "系统中没有该用户，是否需要注册用户？如果需要创建用户请输入：user.add <用户名（请用英文或数字）> <密码（请用英文或数字，如果没有指定密码，默认密码为用户名）>"
					}
				}
				else if (c.startsWith("user.add ")) {
				
					String name = items[1]
					String pwd = name
					if (items.length > 2) {
						pwd = items[2]
					}
					
					Student stu = Student.findByUserName(name)
					
					if (stu) {
						respContext = "用户${name}已经注册过, 用命令：user ${name}  可以查看用户信息"
					} else {
						Student newStu = new Student(userName: name, email: "taylor.liu@qq.com", password: pwd, fullName: name, role: "student", teacher:Teacher.findByUserName("te1"))
						if (newStu.save(failOnError:true)) {
							respContext = "用户${name}注册成功！系统默认给你分配了老师te1，以后可以登录 www.gowithyun.com 完善用户信息"
						} else {
							respContext = "用户${name}注册失败！"
						}
					}
				}
			}
						
			Content {mkp.yieldUnescaped ("<![CDATA[${respContext}]]>")}
		}
		
		return sw.toString()
	}
	
	private String linkUser(xToUserName, xFromUserName, String c)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
			if (!stu) {
				println "This WeiXin openId hasn't been bund"
				if (session.user) {
					Student stuInDB = Student.get(session.user.id)
					stuInDB.weixinOpenid = xFromUserName
					if (stuInDB.save())	{				
						respContext = "您的微信账户与高手云账号已经绑定成功"
					} else {
						respContext = "您的微信账户与高手云账号已经绑定失败"
					}
				} else {
					respContext = "请先登录您的高手云账号"
				}
			} else {
				respContext = "该微信账号已经绑定了高手云账号：${stu.userName} (${stu.fullName})"
			}
						
			Content {mkp.yieldUnescaped ("<![CDATA[${respContext}]]>")}
		}
		
		return sw.toString()
	}
	
	private String unlinkUser(xToUserName, xFromUserName, String c)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
			if (!stu) {
				respContext = "您的微信没有绑定任何高手云账号"
			} else {
				stu.weixinOpenid = null
				if (stu.save(failOnError:true)) {
					respContext = "该微信账号已经解除绑定高手云账号：${stu.userName} (${stu.fullName})"
				} else {
					respContext = "该微信账号解除绑定高手云账号失败：${stu.userName} (${stu.fullName})"
				}				
			}
						
			Content {mkp.yieldUnescaped ("<![CDATA[${respContext}]]>")}
		}
		
		return sw.toString()
	}
	
	private String createLinkUser(xToUserName, xFromUserName, String c)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(xFromUserName)
			if (!stu) {
				println "This WeiXin openId hasn't been bund"
				if (session.user) {
					Student stuInDB = Student.get(session.user.id)
					stuInDB.weixinOpenid = xFromUserName
					if (stuInDB.save())	{				
						respContext = "您的微信账户与高手云账号已经绑定成功"
					} else {
						respContext = "您的微信账户与高手云账号已经绑定失败"
					}
				} else {
					respContext = "请先登录您的高手云账号"
				}
			} else {
				respContext = "该微信账号已经绑定了高手云账号：${stu.userName} (${stu.fullName})"
			}
						
			Content {mkp.yieldUnescaped ("<![CDATA[${respContext}]]>")}
		}
		
		return sw.toString()
	}
	
	/**
	 * 
	 * @param xToUserName
	 * @param xFromUserName
	 * @param commandItems example: kp go on #10
	 * @return
	 */
	private String kpActions(xToUserName, xFromUserName, String[] commandItems)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))			
												
			if (commandItems.length == 1) {
				MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
				Content {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.HELP_INFO}]]>")}
			} 
			else {
				if (commandItems[0].startsWith("kp") && !commandItems[0].startsWith("kp.")) {
					
					MsgType {mkp.yieldUnescaped ("<![CDATA[news]]>")}
										
					def userToSearch = ""
					int pageId = 1
					def lastItem = commandItems[commandItems.length - 1].trim()
					if (lastItem.contains("#")) {
						pageId = Integer.parseInt(lastItem.substring(1))
						if (pageId <= 0) {
							pageId = 1
						}
						
						for (int i=1; i < commandItems.length - 1; i++) {
							userToSearch = userToSearch + " "+commandItems[i]
						}
					}
					else {
						for (int i=1; i < commandItems.length; i++) {
							userToSearch = userToSearch + " "+commandItems[i]
						}
					}
					userToSearch = userToSearch.trim()
					
					def numOfPage = 5
										
					def cr = KnowledgePoint.createCriteria()
					def results = cr.list(max:numOfPage, offset:(pageId - 1)*numOfPage) {
						and {
							ilike("name", "%${userToSearch}%")
							ge("totalQuestion", 0)
						}
						order('totalQuestion', 'desc')
					}
					
					def resultsTotal = KnowledgePoint.where {
						name =~ "%${userToSearch}%" && totalQuestion >= 0
					}
										
					def pageTotal = (int)((resultsTotal.list().size() - 1)/numOfPage) + 1
					if (pageId >= pageTotal)
					{
						pageId = pageTotal
					}
										
					int itemCount = 0  // at least one item in Articles
					def kpIdListStr = ""
					Articles {
						item {
							itemCount++
							if (pageTotal > 0) {
								def pageDesc = "第${pageId}页, 共${pageTotal}页, "
								Title {mkp.yieldUnescaped ("<![CDATA[您查询的知识点：${userToSearch}, ${pageDesc}点击知识点进入练习 ]]>")}
							}
							else {
								Title {mkp.yieldUnescaped ("<![CDATA[您查询的知识点： ${userToSearch} 不存在 ]]>")}
							}
						}
						
						def hasQuestionFlag = false
					
						results.each { kp->
							kpIdListStr += kp.id+"X"
							item {
								itemCount++
								def appendMsg = ""
								if (kp.totalQuestion > 0) {
									hasQuestionFlag = true
								} else {
									appendMsg = " (该知识点暂时没有题目)"
								}
								Title {mkp.yieldUnescaped ("<![CDATA[ ${itemCount-1}: ${kp.name}${appendMsg} ]]>")}
								PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
								if (kp.totalQuestion > 0) {
									Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?openId=${xFromUserName}&kpList=${kp.id}]]>")}
								}
							}
							
						}
									
						if (itemCount > 2 && hasQuestionFlag){
							item {
								itemCount++
								Title {mkp.yieldUnescaped ("<![CDATA[ 练习上面列出的所有知识点 ]]>")}
								PicUrl {mkp.yieldUnescaped ("<![CDATA[https://mmbiz.qlogo.cn/mmbiz/D0LMn244XHCARPMR0BdvybhUYHd34tSC5IHxOiayAGZFI2R4rjlaZkcUEeCU8sBN0W62uicuoib776jEL3HIJwFwQ/0?wx_fmt=png]]>")}
								Url {mkp.yieldUnescaped ("<![CDATA[${WeiXinUtil.WEIXIN_SRV_URL}/practice?openId=${xFromUserName}&kpList=${kpIdListStr}]]>")}
							}
						}
					}
					
					ArticleCount (itemCount)
				}
				else if (commandItems[0].startsWith("kp.l ")) {			
					MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
					Content {mkp.yieldUnescaped ("<![CDATA[Not supported yet]]>")}
				}
				else if (commandItems[0].startsWith("kp.w ")) {					
					MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
					Content {mkp.yieldUnescaped ("<![CDATA[Not supported yet]]>")}
				}
			}		
		}
		
		return sw.toString()
	}
	
	private String questionActions(xToUserName, xFromUserName, String c)
	{
		def sw = new StringWriter()
		
		String respContext = ""
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			String[] items = c.split("\\s+")
						
			if (items.length == 1) {
				respContext = WeiXinUtil.HELP_INFO
			}
			else {
				if (c.startsWith("q.t ")) {
					respContext = "Not supported yet"
				}
				else if (c.startsWith("q.v ")) {
				
					respContext = "Not supported yet"
				}
				else if (c.startsWith("q.a ")) {
					
					respContext = "Not supported yet"
				}
			}
						
			Content {mkp.yieldUnescaped ("<![CDATA[${respContext}]]>")}
		}
		
		return sw.toString()
	}
	
	private String reply(xToUserName, xFromUserName, replyContent)
	{
		def sw = new StringWriter()
		
		def respXML = new groovy.xml.MarkupBuilder(sw)
		respXML.xml {
			ToUserName {mkp.yieldUnescaped ("<![CDATA[${ xFromUserName }]]>")}
			FromUserName {mkp.yieldUnescaped ("<![CDATA[${  xToUserName  }]]>")}
			CreateTime ((int)(System.currentTimeMillis()/1000))
			MsgType {mkp.yieldUnescaped ("<![CDATA[text]]>")}
			
			Content {mkp.yieldUnescaped (replyContent)}
		}
		
		return sw.toString()
	}

	private boolean checkSignature(nonce, timestamp, signature) {
		String token = "OTSWeiXin"

		def a = [token, timestamp, nonce]
		a = a.sort()

		def sortedStr = a.join("")

		println "sortedStr: ${sortedStr}"

		String tmpStr = ""

		println "params.echostr: ${params.echostr}"
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1")
			// crypt.reset();
			// crypt.update(sortedStr.getBytes("UTF-8"));

			byte[] bs = crypt.digest(sortedStr.getBytes("UTF-8"))

			tmpStr = byteToHex(bs);
			println "byteToHex - crypt.digest(): ${tmpStr}"
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (tmpStr == signature) {
			return true
		} else {
			return false
		}
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	// 创建二维码
	def createQRCode(Long id) {
		def TOKEN = WeiXinUtil.getTOKEN()
		
		def url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=${TOKEN}"
		def postData = """{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": ${id}}}}"""
		println "postData is : ${postData}"
		
		// send POST data
		def ticketJson = new JsonSlurper().parseText( WeiXinUtil.sendPost(url, postData) )
		println "ticketJson is : ${ticketJson}"
		
		def showUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode"
						
		redirect(url: showUrl+"?ticket=${ticketJson.ticket.encodeAsURL()}")
	}
	
	/**
	 * 	1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
		2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
		3、创建自定义菜单后，由于微信客户端缓存，需要24小时微信客户端才会展现出来。测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。 
		管理员需要登录 然后点击 “更新微信公众号菜单” 链接手动更新菜单，或者直接触发链接：<WEB_HOME>/weiXin/createMenu
	 * @return
	 */
	def createMenu() {
		def TOKEN = WeiXinUtil.getTOKEN()
		
		def url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=${TOKEN}"
		def postDataOTSV2 = """
{
    "button": [
        {
			"name": "我的中考", 
            "sub_button": [                
				{
                    "type": "click", 
                    "name": "我要成为会员", 
                    "key": "new_wx_user"
                },
				{
                    "type": "click", 
                    "name": "我的注册信息", 
                    "key": "check_binding"
                },
                {
                    "type": "click", 
                    "name": "我的薄弱知识", 
                    "key": "weak_kp"
                }, 
                {
                    "type": "view", 
		            "name": "我要名师辅导", 
		            "url": "http://wingokid.kuaizhan.com/28/66/p3584769061cfc5"
                }
            ]
        }, 
        {
            "type": "scancode_push", 
            "name": "扫一扫", 
            "key": "sao1sao"
        },
		{
            "name": "智能辅导", 
            "sub_button": [
				{
                    "type": "view", 
		            "name": "智能学习", 
		            "url": "http://gshouyun.kuaizhan.com/90/89/p3636400807be5f"
                },
				{
                    "type": "view", 
		            "name": "中考吐槽", 
		            "url": "http://buluo.qq.com/mobile/barindex.html?_wv=1027&_bid=128&from=pc&bid=16065"
                },
				{
                    "type": "view", 
		            "name": "直通名师", 
		            "url": "http://gshouyun.kuaizhan.com/97/91/p3620573558e568"
                }
            ]
		  }
    ]
}
"""
		def postDataKCDB = """
{
    "button": [
        {
			"name": "我的中考", 
            "sub_button": [       
				{
                    "type": "click", 
                    "name": "我要成为会员", 
                    "key": "new_wx_user"
                },         
				{
                    "type": "view", 
		            "name": "我的课程导报", 
		            "url": "http://s6060385057.kuaizhan.com/39/40/p364000266d3233"
                },
				{
                    "type": "click", 
                    "name": "我的薄弱知识", 
                    "key": "weak_kp"
                },
                {
                    "type": "view", 
                    "name": "我的名师辅导", 
                    "url": "http://s6060385057.kuaizhan.com/87/0/p365940978e5ac9"
                }
            ]
        }, 
        {
            "name": "学习社区", 
            "sub_button": [
				{
                    "type": "view", 
                    "name": "少儿绘本精读", 
                    "url": "http://wingokid.kuaizhan.com/50/49/p3602693731a784?from=groupmessage&isappinstalled=0"
                },
				{
                    "type": "view", 
                    "name": "外教睡前故事", 
                    "url": "http://jiazhangdao.kuaizhan.com/39/91/p3242616549fc11"
                },
				{
                    "type": "view", 
                    "name": "中考吐槽论坛", 
                    "url": "http://buluo.qq.com/p/barindex.html?bid=16065"
                },
				{
                    "type": "view", 
                    "name": "名师中考讲座", 
                    "url": "http://m.qlchat.com/live/270000027032876.htm?from=groupmessage&isappinstalled=0"
                }
            ]
        },
		{
            "name": "名师辅导", 
            "type": "view", 
            "url": "http://s6060385057.kuaizhan.com/87/0/p365940978e5ac9"
		}
    ]
}
"""
		def postDate = null
		if (WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB) {
			postDate = postDataKCDB
		}
		else {
			postDate = postDataOTSV2
		}
		
		println "postData is : ${postDate}"
		
		// send POST data
		def ticketJson = new JsonSlurper().parseText( WeiXinUtil.sendPost(url, postDate) )
		println "ticketJson is : ${ticketJson}"		
		
		WeiXinUtil.MENUE_CREATED = true
		render "ticketJson is : ${ticketJson}" 
	}
	
	// current cron job is: 
	// 30 19 * * 2 curl http://121.40.200.161/weiXin/weakKPReport
	def weakKPReport()
	{
		println "weakKPReport go ..."
		WeiXinUtil.weakKPReport(params.testFlag)
		render ("weakKPReport generated :) ")
	}
	
	def test() {
						
		// test for C_FOR_WX
		// <Url><![CDATA[http://121.40.200.161/weiXin/practice?sourceq=17959&openId=oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU&kpList=8962X]]></Url>
		//http://http://localhost:9090/ots/weiXin/practice?sourceq=17959&openId=oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU&kpList=8962X
		
		//------------------------------------
		// println WeiXinUtil.getTOKEN()
		
		//------------------------------------
//		int stuWXCount = Student.count() + 1
//		println "stuWXCount is: ${stuWXCount}"
//		WeiXinUtil.weakKPReport()
		
		//------------------------------------
		//def ret = kpActions("from", "to", "kp take off #0".split("\\s+"))
		//println "kpActions return string: ${ret}"
		
		// println menuClick("a", "b", "new_wx_user")
		
		println scanCode("a", "oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU", "90007")
		// render checkSignature("1589609520", "1470395960", "baf451e89f976a38e73ea5837fa1e3d54f48f810")
		// render WeiXinUtil.getTOKEN()
		new_wx_user()
	}
	
	def video() {
		//println "this is video controller, vid=${params.vid}..."
		[vid: params.vid]
	}
	
//	def video2() {
//		//println "this is video controller, vid=${params.vid}..."
//		[vid: params.vid]
//	}
	

	def practice() {
		// considering to merge with QuizController.focusGenerate()
		def openId
		Student stu

		if (!session.user) {
			openId = params.openId
			println "No session.user found in action WeiXinController.practice(), change to use params.openId"
			stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(params.openId)
			if (!stu) {
				render """
<font size='8'>这么牛的提分神奇都被你发现了啊，你现在不能做题是因为还没有找到给你三颗痣的人，我来教你找到紫霞仙子吧！
<p>第一步：找到本页面有右下角的“会员”菜单；
<p>第二步：点击“注册学生会员”
<p>完成以上两步，你就可以智能做题了。</font>
"""
				return
			}
				
			session.user = stu
		} 
		else {
			stu = Student.get(session.user.id)
			openId = stu.weixinOpenid
		}
				
		String[] kpIds = params.kpList?.split("X")
		def kps = []
		
		def exampleQues = null
		if (!kpIds || kpIds?.length < 1 || kpIds[0]?.trim() == "") {
			render "<font size='8'>params.kpList是空值，没有找到知识点需要练习的题目</font>"
			return
		} else {
			kpIds.each { kpid->
				KnowledgePoint kp = KnowledgePoint.get(Integer.parseInt(kpid))
				kps << kp
				kp.questions.each { ques->
					if (ques) {
						exampleQues = ques
					}
				}
			}
		}
						
		session.numberOfQuestionsPerPage = 1
				
		Assignment assi = null
		def nameTag = null
		// check if 阅读理解 or 填空选择
		Question sourceQuestion = Question.get(params.sourceq) // 如果是来自于学生做某个练习时候，扫描了练习题前面的二维码
		// if 阅读理解
		if (sourceQuestion?.childQuestions?.size() || exampleQues?.childQuestions?.size()) {
			assi = Assignment.findByNameAndAssignedBy("B_FOR_WX", "tewx")
			if (!assi) {
				assi = new Assignment(subject:CONSTANT.COURSE_ENG2, name:"B_FOR_WX", assignedBy:"tewx", totalKnowledgePoints:1, numberOfQuestionsPerPage:1)
			}
			nameTag = "Y"
		} else if (sourceQuestion?.parentQuestion || exampleQues?.parentQuestion) { // 当前 sourceQuestion是个阅读理解的childQuestion
			assi = Assignment.findByNameAndAssignedBy("C_FOR_WX", "tewx")
			if (!assi) {
				assi = new Assignment(subject:CONSTANT.COURSE_ENG2, name:"C_FOR_WX", assignedBy:"tewx", totalKnowledgePoints:1, numberOfQuestionsPerPage:1)
			}
			nameTag = "C"
		}
		else {
			assi = Assignment.findByNameAndAssignedBy("A_FOR_WX", "tewx")
			if (!assi) {
				assi = new Assignment(subject:CONSTANT.COURSE_ENG1, name:"A_FOR_WX", assignedBy:"tewx", totalKnowledgePoints:1, numberOfQuestionsPerPage:1)
			}
			nameTag = "M"
		}
		
		assi.save()
		
		def name = nameTag+(Quiz.countByStudentAndAssignment(stu, assi) + 1)
					
		AssignmentStatus aStatus = AssignmentStatus.findByStudentAndAssignment(stu, assi)
		if (!aStatus) {
			aStatus = new AssignmentStatus(student:stu, assignment:assi, status:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN ).save()
		}
		session.assignment = assi
		session.assignmentStatus = aStatus
		
		Quiz newQuiz = new Quiz(student:stu, name:name, assignment:assi, status:CONSTANT.QUIZ_STATUS_NOTBEGIN, type:CONSTANT.QUIZ_TYPE_SELFPRAC)		
				
		int questionNumLimit = 5;
		Random randomizer = new Random()
		
		def selectedQMap = [:] as HashMap
		
		for (int qindex=0; qindex < questionNumLimit; qindex++) {
			
			KnowledgePoint kp
			Question selectedQ
			
			// loop for one selected question
			for (int i=0; i<kps.size()*2; i++) {
				// pick a KP
				int pickedKPIndex = randomizer.nextInt(kps.size())
				kp = kps[pickedKPIndex]
				
				// store Question id for select randomly
				def qList = []
				
				kp.questions.each {
					qList << it.id
				}
				
				// for C_FOR_WX, we don't go through the child KPs
				if (assi.name != 'C_FOR_WX') {
					kp.childPoints.each { ckp1 ->
						ckp1.questions?.each { ques1 ->
							qList << ques1.id
						}
						
						ckp1.childPoints.each { ckp2 ->
							ckp2.questions?.each { ques2 ->
								qList << ques2.id
							}
						}
					}
				}
											
				if (qList.size() > 0) {
					int selectedIndex = randomizer.nextInt(qList.size())
					selectedQ = Question.get(qList.get(selectedIndex))
					// we can repeat questions, don't use: && !stu.practiceHistory.contains(selectedQ.qID+" ")
					if (selectedQ.id != sourceQuestion?.id && !selectedQMap.containsKey(selectedQ)  ) {
						if (selectedQ.parentQuestion) {
							selectedQ = selectedQ.parentQuestion
							// for 阅读理解，出一道题即可
							questionNumLimit = 1
						}
						if (selectedQ.childQuestions?.size() > 0) {
							questionNumLimit = 1
						}
					
						selectedQMap.put(selectedQ, kp)
						break
					} else {
						selectedQ = null
					} 
				}								
			}						
		}
		
		def msg = ""
		// if selected question number is less than the limit, try to check KPs' 2 level parents
		if (selectedQMap.size() < questionNumLimit) {
			println "current selectedQMap.size(): ${selectedQMap.size()}"
			msg = "您所选择的知识点题目比较少，系统为您寻找了上级知识点的相关练习题"
			
			// store Question id for select randomly
			def qList = []
			def kpList = []
			kps.each { kp ->
				println "kp: ${kp}"
				kp.parentPoints.each { parent ->
					parent.questions.each {  ques->
						qList << ques.id
						kpList << parent
					}
					
					parent.parentPoints.each { parent2 ->
						parent2.questions.each {  ques->
							qList << ques.id
							kpList << parent2
						}
					}
				}
			}
			
			int sumQnum = qList.size()
			// we can repeat questions for different quiz in WeiXin, so we don't check stu.practiceHistory
			if (sumQnum > 0) {
				for (int i=0; i < questionNumLimit * 2 && selectedQMap.size() < questionNumLimit; i++) {
					int selectedIndex = randomizer.nextInt(sumQnum)
					Question selectedQ = Question.get(qList.get(selectedIndex))
					// we can repeat questions, don't use: && !stu.practiceHistory.contains(selectedQ.qID+" ")
					if (selectedQ.id != sourceQuestion?.id && !selectedQMap.containsKey(selectedQ) ) {
						selectedQMap.put(selectedQ, kpList.get(selectedIndex))
					}
				}
			}			
		}
		
		int questionNum = selectedQMap.size()
		
		if (selectedQMap.size() > 0) {
			newQuiz.totalQuestionNum = questionNum
			newQuiz.save()
			selectedQMap.each { selectedQ, kp ->
				newQuiz.addToRecords(new QuizQuestionRecord(quiz:newQuiz, question:selectedQ, chosenFor: kp).save())
			}
			
			newQuiz.save()
			redirect(controller:"quizQuestionRecord", action:"answering", 
				params:[quiz_id:newQuiz.id, openId:params.openId, kpList: params.kpList, sourceq:params.sourceq, msg:msg])
		} 
		else {
			render "<font size='5'>没有找到需要练习的题目</font>"
		}		
	}
	
	def parents_stu_link(Long id)
	{
		def parentsName = ""
		def stuName = ""
		
		if (params.stuName && params.parentsName) {
			parentsName = params.parentsName.trim()
			stuName = params.stuName.trim()
			
			Student stu = Student.findByUserName(stuName)
			if (stu) {
				EndUser p = EndUser.findByUserNameAndRole(parentsName, 'parents')
				if (p) {
					stu.parents = p.userName
					flash.message = "parents ${parentsName} and student ${stuName} linked"
					stu.save()
				}
				else {
					flash.message = "WARNING: parents account user name ${parentsName} not found in system"
				}
			}
			else {
				flash.message = "WARNING: student account user name ${stuName} not found in system"
			}
		}
		else {
			EndUser currentLoginUser = EndUser.get(id)
			if (!currentLoginUser) {
				flash.message = "No user found"
			}
			else {
				if (currentLoginUser.role == "parents") {
					parentsName = currentLoginUser.userName
				}
				else if (currentLoginUser instanceof Student) {
					stuName = currentLoginUser.userName
				}
				else {
					flash.message = "The current user is neither Student nor Parents"
				}
			}
		}
		
		[stuName:stuName, parentsName:parentsName]
	}
	
	def new_wx_user()
	{
		def opflag = params.opflag?.trim()
		def openId = params.openId?.trim()
		
		def stuName = params.stuName?.trim()
		def parentsName = params.parentsName?.trim()
		
		def alreadyBound = [:]
		def readOnlyField = null
				
		EndUser currentLoginUser = (openId == null ? null : EndUser.findByWeixinOpenidAndWeixinOpenidIsNotNull(openId) ) // will return the first value if openId is null, why?
		if (currentLoginUser) {
			flash.forUserMsg = "您的微信号已经绑定了高手云账号: ${currentLoginUser.userName}"
			if (currentLoginUser instanceof Student) {
				if (!currentLoginUser.parents) {
					flash.forUserMsg += ", 您是学生用户，但还未绑定家长帐号"
					alreadyBound << ["stu":true]
				}
				else {
					flash.forUserMsg += ",  您是学生用户，家长帐号是 ${currentLoginUser.parents}"
					alreadyBound << ["parents_stu":true]
				}
			}
			else if (currentLoginUser.role == "parents") {
				flash.forUserMsg = ", 您是家长用户"
				alreadyBound << ["parents":true]
			}
			else {
				flash.forUserMsg = "您不是学生用户，也不是家长用户。(currentLoginUser=${currentLoginUser})"
			}
			
			if (opflag == "parents_stu_binding") {				
				if (currentLoginUser.role == "parents") {
					parentsName = currentLoginUser.userName
					readOnlyField = "parentsName"
				}
				else if (currentLoginUser instanceof Student) {
					stuName = currentLoginUser.userName
					readOnlyField = "stuName"
				}
				else {
					flash.forUserMsg = "The current user is neither Student nor Parents"
				}
			}
		} 
		
		if (openId == null && opflag != "ask_for_help") {
			opflag = "no_valid_openId"
			flash.forUserMsg = "Error: 没有正确获得微信openId"
		}
		
		int currentMaxEndUserID = 0
		
		if (openId && opflag) {
			def c = EndUser.createCriteria()
			currentMaxEndUserID = c.get { projections { max('id') } } + 1
			
			if (opflag == "new_wx_stu" && stuName && !alreadyBound['stu']) {
				def uname = stuName
				Student stu = new Student(userName: uname, password: uname, fullName: uname, role: "student", email:"weixin@weixin.com", teacher:Teacher.findByUserName("te"))
				stu.weixinOpenid = openId
								
				if (stu.save()) {
					flash.forUserMsg = "为您开通高手云学生账号：${uname}，并且绑定微信账号成功"
				} else {
					flash.forUserMsg = "为您开通高手云学生账号：${uname}，并且绑定微信账号失败, errors:"+stu.errors
				}
				
				alreadyBound << ["stu" : true]
			}
			else if (opflag == "new_wx_parents" && parentsName && !alreadyBound['parents']) {
				def uname = parentsName
				EndUser parents = new EndUser(userName: uname, password: uname, fullName: uname, role: "parents", email:"weixin@weixin.com")
				parents.weixinOpenid = openId
								
				if (parents.save()) {
					flash.forUserMsg = "为您开通高手云家长账号：${uname}，并且绑定微信账号成功"
				} else {
					flash.forUserMsg = "为您开通高手云家长账号：${uname}，并且绑定微信账号失败, erros:"+parents.errors
				}
				
				alreadyBound << ["parents" : true]
			}
			else if (opflag == "parents_stu_binding" && stuName && parentsName && !alreadyBound['parents_stu']){
								
				Student stu = Student.findByUserName(stuName)
				if (stu) {
					EndUser p = EndUser.findByUserNameAndRole(parentsName, 'parents')
					if (p) {
						stu.parents = p.userName
						flash.forUserMsg = "parents ${parentsName} and student ${stuName} linked"
						stu.save()
					}
					else {
						flash.forUserMsg = "WARNING: parents account userName ${parentsName} not found in system"
					}
				}
				else {
					flash.forUserMsg = "WARNING: student account userName ${stuName} not found in system"
				}
				
				alreadyBound << ["parents_stu" : true]
			}
			else if (opflag == "ask_for_help") {
				flash.forUserMsg = "技术支持电话：18948358865"
			}
			else {
				println "Error: could not recognize opflag=${opflag}"
			}
		}
		
		println "flash.forUserMsg = ${flash.forUserMsg}， currentMaxEndUserID=${currentMaxEndUserID}"
		[opflag:opflag, openId:openId, stuName:stuName, parentsName:parentsName, alreadyBound:alreadyBound, currentMaxEndUserID:currentMaxEndUserID, readOnlyField:readOnlyField]
	}
	
	def new_wx_user_done()
	{
		// just redirect to new_wx_user_done.gsp to show the binding results
	}
	
	def qrCodeFor()
	{
		def checkFor = params.checkFor
		def qrCode = params.qrCode
	}
		
			
	// ===================================================================================

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [weiXinInstanceList: WeiXin.list(params), weiXinInstanceTotal: WeiXin.count()]
    }

    def create() {
        [weiXinInstance: new WeiXin(params)]
    }

    def save() {
        def weiXinInstance = new WeiXin(params)
        if (!weiXinInstance.save(flush: true)) {
            render(view: "create", model: [weiXinInstance: weiXinInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), weiXinInstance.id])
        redirect(action: "show", id: weiXinInstance.id)
    }

    def show(Long id) {
        def weiXinInstance = WeiXin.get(id)
        if (!weiXinInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "list")
            return
        }

        [weiXinInstance: weiXinInstance]
    }

    def edit(Long id) {
        def weiXinInstance = WeiXin.get(id)
        if (!weiXinInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "list")
            return
        }

        [weiXinInstance: weiXinInstance]
    }

    def update(Long id, Long version) {
        def weiXinInstance = WeiXin.get(id)
        if (!weiXinInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (weiXinInstance.version > version) {
                weiXinInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'weiXin.label', default: 'WeiXin')] as Object[],
                          "Another user has updated this WeiXin while you were editing")
                render(view: "edit", model: [weiXinInstance: weiXinInstance])
                return
            }
        }

        weiXinInstance.properties = params

        if (!weiXinInstance.save(flush: true)) {
            render(view: "edit", model: [weiXinInstance: weiXinInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), weiXinInstance.id])
        redirect(action: "show", id: weiXinInstance.id)
    }

    def delete(Long id) {
        def weiXinInstance = WeiXin.get(id)
        if (!weiXinInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "list")
            return
        }

        try {
            weiXinInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'weiXin.label', default: 'WeiXin'), id])
            redirect(action: "show", id: id)
        }
    }
}
