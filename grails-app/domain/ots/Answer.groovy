package ots

class Answer {

	String serialNum
	String content
	boolean correct
	boolean deleted
	
	static transients = [ 'deleted' ]
	String toString () {
		"${serialNum}: ${content} - ${correct}"
	}
	
	String showInQuestion() {
		return "${serialNum}: ${content}"
	}
	
	static belongsTo = [question : Question]
	
    static constraints = {
		//serialNum(inList: ["A", "B", "C", "D", "E", "F", "1", "2", "3", "4", "5", "6", "7", "8", "9"])
		//content blank : false
		serialNum(nullable:true)
		content(nullable:true)
		deleted bindable: true
    }
	
	boolean checkUserAnswer(String answerByUser)
	{
		if (answerByUser == null)
		{
			return false
		}
		
		if (question.type == CONSTANT.RADIO_QUESTION) {
			return correct && id+"" == answerByUser.trim()
		} else if (question.type == CONSTANT.FBLANK_QUESTION) {
		    String[] cOptions = content.split("\\|\\|")
		    if (cOptions.length > 1) {
				boolean passFlag = false
				cOptions.each {
					// println("DEBUG: check option answer: "+it)
					if (correct && it.trim() == answerByUser.trim()) {
						passFlag = true
					}
				}
				return passFlag
			} 
			else {
			  return correct && content.trim() == answerByUser.trim()
			}
		}
	}
}
