package ots

class StuAnswer {
	Answer refAnswer // reference answer
	String userInput
	// TODO correct default value is false: boolean correct = false
	boolean correct
	
	static belongsTo = [quizQuestionRecord: QuizQuestionRecord]

	static constraints = {
		refAnswer()
		userInput(nullable:true, blank:true)
		correct()
	}

	static mapping = {
		refAnswer index: 'refAnswer_idx'
	}
	
	boolean isCorrect() {
		if (!userInput) {
			return false
		}

		return refAnswer.checkUserAnswer(userInput)
	}
}
