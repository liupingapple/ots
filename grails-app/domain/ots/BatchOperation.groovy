package ots

import java.util.Date;

class BatchOperation {

	String batchOperation
	Date dateCreated

	Integer startIndex = 1
	Integer endIndex = 10000
	String startNode
	Integer traverseDepth = 5

	String columnSelector = "degree, masterRatio, associatedQuestion, totalQuestion"
	String batchInput
	String batchOutput
	String batchResult
	String batchError
	
	static transients = ["batchInput", "batchOutput"]
	
	static constraints = {
		batchOperation(nullable: false)
		startIndex(nullable: true)
		endIndex(nullable: true)
		columnSelector(nullable: true)
		startNode(nullable: true)
		traverseDepth(nullable: true)
		batchInput(bindable: true)
		batchOutput(bindable: true)
		batchResult(nullable: true)
		batchError(nullable: true)
	}
}
