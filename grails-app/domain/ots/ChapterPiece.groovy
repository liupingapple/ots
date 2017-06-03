package ots

class ChapterPiece {
	// 考点辨析1，考点辨析2，。。。
	String name
	
	String content
	
	static belongsTo = [chapter : Chapter]

    static constraints = {
		name(nullable:false)
		content(nullable:true, maxSize:1500)
    }
	
	@Override
	public String toString() {
		return "${chapter} ${name}"
	}
}
