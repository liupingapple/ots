package ots

import java.util.Date;

// TO-DO: Integrate it into the BatchOperation class
class BatchKnowledgePointOp {

    Date dateCreated
	String extraFields = "degree, masterRatio, associatedQuestion, totalQuestion"
	String startNode
	Integer traverseDepth = 10
	String batchContent
	String operation
	Integer startIndex = 1
	Integer endIndex = 10000
	
	static transients = ["batchContent"]
	
    static constraints = {
		operation(nullable: false, inList: ["Export node content", "Export child relationship", 
											"Export parent relationship", "Import node content",
											"Import child relationship", "Import parent relationship"])
		extraFields(bindable: true)
		startNode(nullable: true)
		traverseDepth(nullable: true)
		batchContent(bindable: true)
    }	
}
