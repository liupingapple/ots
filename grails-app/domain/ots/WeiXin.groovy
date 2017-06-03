package ots
/**
 * 微信公众平台接口
 * @author taliu
 *
 */
class WeiXin {

	String openId
	String latestMenuClick
	boolean input4menuClick = false
	
    static constraints = {
		openId (nullable:false, unique: true)
		latestMenuClick (nullable : true)
    }
}
