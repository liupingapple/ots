package ots

import java.util.Date;
import java.util.List;

// TO-DO: Integrate it into BatchOperation
class BatchQuestionOp {

	String type = "单选"
	String source
	String term
	int errorRate
	Date inputDate
	String reviewedBy
	Date dateCreated
	String extraFields = "source, term, errorRate, reviewedBy, inputBy, inputDate"
	Integer startIndex = 1
	Integer endIndex = 10000
	String batchContent
	String operation
	AdminUser inputBy

	static transients = ["batchContent"]
	static belongsTo = [inputBy : AdminUser] // Need to change it to a String
	
    static constraints = {
		operation(nullable: false, inList: ["Export in Excel format", "Export in custom format", "Export Question - Knowledge mapping", 
											"Import in Excel format", "Import in custom format", "Import Question - Knowledge mapping",])
		type(nullable: false, blank: true, inList: CONSTANT.QUESTION_TYPEs)
		extraFields(bindable: true)
		startIndex(nullable: true)
		endIndex(nullable: true)
		source()
		term()
		errorRate()
		inputBy()
		inputDate()
		reviewedBy()
		batchContent(bindable: true)
    }
}
