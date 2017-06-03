package ots

class WeiXinReply {

	String keyWords
	String replyContent
		
    static constraints = {
		// keyWords unique:['replyContent'], blank:false, maxSize:100
		keyWords unique:true, blank:false, maxSize:100
		replyContent blank:false, maxSize:2000
    }
}
