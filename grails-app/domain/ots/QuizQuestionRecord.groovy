package ots

import java.math.BigDecimal;

class QuizQuestionRecord {	
	Question question
	String briefComment
	KnowledgePoint chosenFor
		
	// TO-DO: Do we need to save some KP related information here, instead of another round trip to KPs?
	// TODO: I think we no need to do so, quizQuestionRecord.chosenFor.XX is very easy to access KP related information

	// ForV2: 笔记，学生和老师都可以对该题目做笔记
	String stuNote  // 学生笔记
	String teNote   // 老师笔记或者老师评语
		
	// ForV2: 收藏标记，学生可以将题目收藏，下次复习时候用
	boolean favoriteFlag = false
		
	// ForV2: 截止到当前这个练习quiz，知识点的Question完成覆盖率
	BigDecimal coverageRate = 0
	
	static belongsTo = [quiz: Quiz]
	static hasMany = [stuAnswers: StuAnswer] // for 填空题, one question may have more than one student answer
	
    static constraints = {
		briefComment(nullable:true, blank:true, maxSize:1000)
		chosenFor(nullable:true, blank:true)
		stuNote(nullable:true, maxSize:400)
		teNote(nullable:true, maxSize:400)
		favoriteFlag(nullable:true)
		coverageRate(nullable:true)
    }
	
	@Override
	String toString() {
		def str = question.description
		str.substring(0,Math.min(45, str.length()))+" ..."
	}
}
