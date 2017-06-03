package ots

class Chapter {
	String name
	String bookName
	String qrCode
		
	String comment
	
	static hasMany = [assignment: Assignment, chapterPieces: ChapterPiece]

    static constraints = {
		name(nullable:false)
		bookName(nullable:true)
		comment(nullable:true)
		qrCode(nullable:true, validator: { return it == null || (Integer.parseInt(it) >= 90001 && Integer.parseInt(it) <= 93000) })
    }
	
	@Override
	public String toString() {
		return name
	}
}
