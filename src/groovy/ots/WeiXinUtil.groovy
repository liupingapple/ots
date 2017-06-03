package ots
import org.springframework.core.io.support.PropertiesLoaderUtils;

import groovy.json.JsonSlurper

class WeiXinUtil {

	public static final String WeiXinName_KCDB = "KCDB"
	public static final String WeiXinName_OTSV2 = "OTSV2"
	
	// 微信公众平台号
	public static String WeiXinName = null
	public static String AppID = null
	public static String AppSecret = null
	public static String WEIXIN_PM = null	
	public static String ADMIN_OPENID = null	
	public static String WEB_HOME = null
	public static String WEIXIN_SRV_URL = "${WEB_HOME}/weiXin"
	
	static {
		// For OTSV2
		// WEIXIN_PM = "gh_33818a38fbfb"
		// adminOpenId = "oU_kgs4ssZ6GnIeH6m6kpJ9C8AdU"
		// WEB_HOME = "http://121.40.200.161"  
		
		// WEB_HOME = "http://121.40.195.96"  // For KCDB
		println "WeiXinUtil ..." // webapp start will not init WeiXinUtil, WeiXinController will be initialized in webapp start
		String confFile = System.getProperty("user.home")+File.separator+"ots.properties"		
		Properties props = new Properties()
		props.load(new FileReader(confFile))
		println "${confFile}: ${props}"
		WeiXinName = props.getProperty("WEIXIN_NAME")
		AppID = props.getProperty("APP_ID")
		AppSecret = props.getProperty("APP_SECRET")
		WEIXIN_PM = props.getProperty("WEIXIN_PM")
		ADMIN_OPENID = props.getProperty("ADMIN_OPENID")
		WEB_HOME = props.getProperty("WEB_HOME")
		
		// web site for WeiXin
		WEIXIN_SRV_URL = "${WEB_HOME}/weiXin"
	}	
	
	public static final String SubscribeKeyWords4WeiXinReply = "subscribe"
	
	public static final String HELP_INFO = """<![CDATA[使用说明：
- 查询用户:  
   user <用户名>
- 查询知识点:	
   kp <知识点关键字> <#页码(可选)>
]]>"""
	
/**
 * - 注册用户:  
   user.add <用户名> <密码>
	- 查询知识点的练习:
	   kp.q
	- 查询已经练习了的知识点:
	   kp.l
	- 生成薄弱知识点的练习:
	   kp.w	
	- 查看练习文本解说:  
	   q.t  <练习编号>
	- 查看练习视频解说:
	   q.v  <练习编号>
	- 查看练习答案:
	   q.a  <练习编号>
	- 查看练习解说视频:
	   <微信扫描练习二维码>
 */

	public static MENUE_CREATED = false

//	public static String get_access_token()
//	{
//		// need to download HTTPBuilder from http://groovy.codehaus.org/modules/http-builder
//		//def http = new groovyx.net.http.HTTPBuilder( 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET' )
//		// use below sendGet and sendPost instead
//	}
	
	private static long latestTOKENMillis = 0
	private static String TOKEN = ""
	
	public static String getTOKEN()
	{
		long curr = System.currentTimeMillis()
				
		if (curr - latestTOKENMillis > 7200 * 1000) {
			def url = "https://api.weixin.qq.com/cgi-bin/token"
			def oldToken = TOKEN
			synchronized (TOKEN) {
				// if other thread has generated new TOKEN, we no need to re-generate anymore
				if (oldToken == TOKEN) {					
					// TOKEN = sendGet(url, "grant_type=client_credential&appid=wxcd1adcc9dd893ce8&secret=b078c8f00ec4f354ca1148ff9fcf522d")
					def purl = url+"?grant_type=client_credential&appid=${AppID}&secret=${AppSecret}"
					def tk = new JsonSlurper().parseText(purl.toURL().text)
					TOKEN = tk.access_token
					
					latestTOKENMillis = curr
				}
			}
		}
		
		return TOKEN
	}

	// we can use string.toURL().text insteadly
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader reader = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			/*
			 * Map<String, List<String>> map = connection.getHeaderFields(); //
			 * 遍历所有的响应头字段 for (String key : map.keySet()) {
			 * System.out.println(key + "--->" + map.get(key)); }
			 */
			// 定义 BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader reader = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			// ((HttpURLConnection)conn).setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8"); //?
			conn.setRequestProperty("contentType", "UTF-8"); //?
			conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8"); //?
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true); //Triggers POST
			conn.setDoInput(true); 
			
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			
			// 发送请求参数						
			out.print(param);
			
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader( new InputStreamReader(conn.getInputStream())); // 此处加入UTF-8, 是否解决了乱码问题？NO
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		println "post result: ${result}" // the result looks like: {"errcode":0,"errmsg":"ok","msgid":210875687}
		return result 
	}
	
	public static void weakKPReport(testFlag)
	{
		Date d = new Date()
		println "push weak KPs report for every weixin users at ${d}"
		
		println "TO get user list"
		String token = getTOKEN()
		String getRequest = "https://api.weixin.qq.com/cgi-bin/user/get"
		String userListStr = sendGet(getRequest, "access_token=${token}")
		
		def userListJoson = new JsonSlurper().parseText( userListStr )
		//println userListJoson.dump()
		
		println "userListJoson.data.openid : ${userListJoson.data.openid}"
		userListJoson.data.openid.each {
			/*if (adminOpenId == it.toString()) {
				println "admin openId, no need to send report for this user"
			}
			else {
			}*/
			
			Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(it)
			
			println "To send weekly weak KP report to openid: ${it}, student: ${stu}"
			if (stu) {
			
				if (testFlag) {//  if testFlag, only send report to developer's weixin (taliu's)
					if ( (WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB && stu.userName != 'wx30')
						|| (WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_OTSV2 && stu.userName != 'wx14') ) {
						println "for test phase, only send msg to developer, KCDB is wx30, OTSV2 is wx14"
						return						
					}
				}
								
				def contentToSend = []
				// use stu?.aStatus?.each {} may get LazyInitializationException: failed to lazily initialize a collection of role: ots.Student.aStatus
				def aStatus = AssignmentStatus.findAllByStudent(stu)
				aStatus?.each {
					if (it.toBeFocusedKnowledge) {
						contentToSend << it.toBeFocusedKnowledge
					}
				}
				
				// at least two openid, 至少发送给2个用户，adminOpenId是冗余帐号
				/*
				def sendData = """
							{
							   "touser":[
								"${it}",
								"${adminOpenId}"
							   ],
								"msgtype": "text",
								"text": { "content": "Hi ${stu.fullName}, the weak KPs of this week: ${contentToSend}"}
							}
							"""
				WeiXinUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=${token}", sendData)
				*/
				
				// 用预览方式，则touser 不必至少2个的要求：
				/* def sendData = """
							{
							   "touser":"${it}",
								"msgtype": "text",
								"text": { "content": "Hi ${stu.fullName}, the weak KPs of this week: ${contentToSend}"}
							}
							"""
				WeiXinUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=${token}", sendData)
				println "sended results: ${results}"
				*/
				
				// 用模板信息发送
				String v1 = "本周${stu.fullName}小朋友的薄弱知识点报告出炉了" //URLEncoder.encode("本周${stu.fullName}小朋友的薄弱知识点报告出炉了", "UTF-8")
				String v2 = contentToSend.toString() // URLEncoder.encode(contentToSend.toString(), "UTF-8")
				
				String templateId = ""
				if (WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_OTSV2) {
					templateId = "v2xtrRK7bSX-lxRAhH-tcnl1GUW857hycGHGmqASr0c"
				} else if (WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB) {
					templateId = "m8DHl5W0Yw4usxOiupe-cm8fbCfeJTtVgXNyJZS2OwM"
				}
				
				def sendData = """
{
   "touser":"${it}",
   "template_id": "${templateId}",
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
				WeiXinUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${token}", sendData)
				println "sended weakKPReport content to student: ${contentToSend}"
				
				// --------- send report to parent ...
				EndUser parents = EndUser.findByUserName(stu.parents)
				if (parents?.weixinOpenid) {
					def sendData4parents = """
					{
					   "touser":"${parents?.weixinOpenid}",
					   "template_id": "${templateId}",
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
										
					WeiXinUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${token}", sendData4parents)
				}
				
				Thread.sleep(3*1000); // 群发任务需要些时间
			}
		}
	}
	
}
