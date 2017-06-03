package ots

class Util {
	public static token1 = "%%%"
	public static token2 = "###"

	/**
	 * Format: 时间戳###老师给老师的最新留言%%%时间戳###学生给老师的最新留言
	 * @param stu student instance
	 * @return full string of instructed records
	 */
	public static List parseLeaveMsg(Student stu) {
		if (!stu || !stu.telephone2) {
			return ["","","",""]
		}
		
		def ret = []

		String[] lines = stu.telephone2.split(token1)
		String[] teacherMsg = null
		String[] stuMsg = null
		
		if (lines.length >= 1) {
			if (lines[0]) {				
				teacherMsg = lines[0].split(token2)
			}
		}
		
		if (lines.length >= 2) {
			
			if (lines[1]) {
				stuMsg = lines[1].split(token2)
			}
		}
		 
		if (teacherMsg) {
			if (stuMsg) {
				ret = teacherMsg + stuMsg
			} else {
				ret = teacherMsg + ["", ""]
			}
		} else {
			ret = ["",""] + stuMsg
		}
		
		ret = ret.flatten()
		
		ret
	}

	/**
	 * Format: 辅导日期###辅导开始时间###辅导结束时间###涵盖知识点###评价星级###评价详情%%%辅导日期###辅导开始时间###辅导结束时间###涵盖知识点%%% ...
	 * @param stu student instance
	 * @return full string of instructed records
	 */
	public static String parseInstructedRecords(Student stu, boolean showEvaluate) {		
		if (!stu || !stu.briefComment) {
			return null
		}
						
		def content = ""

		String[] lines = stu.briefComment.split(token1)
		int rowId = lines.length
		for (String line : lines) {
			if (line) {
				
				String[] vals = line.split(token2)

				if (vals.length >= 4) {

					content += (rowId--)+":\t"+vals[0]+" "
					content += "  "+vals[1]+" -"
					content += " "+vals[2]+"  |"
					content += "  "+vals[3]
					
					if (showEvaluate && vals.length >= 5) {
						content += "\n\t<评价>："
						content += "&#9733;".multiply(Integer.valueOf(vals[4])).decodeHTML()
						content += "&#x2606;".multiply(5-Integer.valueOf(vals[4])).decodeHTML()
						if (vals.length >=6) {
							content += "  "+vals[5]
						}
					}

					content += "\n"
				} else {
					stu.briefComment = stu.briefComment.replace(line, "")
					stu.save()
				}
			}
		}
		
		content
	}
	
	public static String getLastRecord(Student stu, int fldIndex) {
		if (stu?.briefComment) {
			String[] lines = stu.briefComment.split(token1)
			def line = lines[0]
			if (line) {
				
				String[] vals = line.split(token2)
	
				if (vals.length >= fldIndex) {
					return vals[fldIndex-1]
				}
			}
		}

		return ""
	}
	
	/**
	 * qrCode for 
	 * - Question: 1-60000, 
	 * - KnowledgePoint: 60001-80000, 
	 * - Assignment: 80001-90000, 
	 * - Chapter:90001-93000, 
	 * - notUsed:93001-100000 
	 * @param className
	 * @param qrCode
	 * @return
	 */
	public static isValidQrCode(String className, String qrCode)
	{
		if (!qrCode) {
			return [true, ""] // don't check if NO qrCode	
		}
				
		def rangeMsg = """ qrCode range for reference:
			* Question:       1-60000, 
			* KnowledgePoint: 60001-80000, 
			* Assignment:     80001-90000, 
			* Chapter:        90001-93000, 
			* notUsed:        93001-100000 
		"""
						
		if (className.equals(Question.class.getName())) {
			if (Integer.parseInt(qrCode) < 1 || Integer.parseInt(qrCode) > 60000) {
				return [false, "Error qrCode=${qrCode}: ${rangeMsg}"]
			} else {
				println "qrCode validation passed for ${className}: qrCode=${qrCode}, ${rangeMsg}"
			} 
		} else if (className.equals(KnowledgePoint.class.getName())) {
			if (Integer.parseInt(qrCode) < 60001 || Integer.parseInt(qrCode) > 80000) {
				return [false, "Error qrCode=${qrCode}: ${rangeMsg}"]
			}
			else {
				println "qrCode validation passed for ${className}: qrCode=${qrCode}, ${rangeMsg}"
			} 
		}
		else if (className.equals(Assignment.class.getName())) {
			if (Integer.parseInt(qrCode) < 80001 || Integer.parseInt(qrCode) > 90000) {
				return [false, "Error qrCode=${qrCode}: ${rangeMsg}"]
			}
			else {
				println "qrCode validation passed for ${className}: qrCode=${qrCode}, ${rangeMsg}"
			}
		}
		else if (className.equals(Chapter.class.getName())) {
			if (Integer.parseInt(qrCode) < 90001 || Integer.parseInt(qrCode) > 93000) {
				return [false, "Error qrCode=${qrCode}: ${rangeMsg}"]
			}
			else {
				println "qrCode validation passed for ${className}: qrCode=${qrCode}, ${rangeMsg}"
			}
		}
		else {
			return [false, "Could not recognize className=${className} for qrCode=${qrCode} validation"]
		}
		// 93001-100000 预留
				
		return [true, "qrCode validation passed"]
	}
	
//	public static groovy.sql.Sql getExpSql()
//	{
//		groovy.sql.Sql sql = groovy.sql.Sql.newInstance(
//			"jdbc:h2:web-app/exp/studata;MVCC=FALSE;LOCK_TIMEOUT=10000;MODE=PostgreSQL;LOG=2;TRACE_LEVEL_FILE=2;TRACE_LEVEL_SYSTEM_OUT=1",
//			"sa",
//			"",
//			"org.h2.Driver")
//		
//		return sql
//	}
//	
//	public static groovy.sql.Sql getImpSql()
//	{
//		// when H2 DB exists and open readonly;ACCESS_MODE_DATA=r;?
//		groovy.sql.Sql sql = groovy.sql.Sql.newInstance(
//			"jdbc:h2:web-app/imp/studata;IFEXISTS=TRUE;LOCK_TIMEOUT=10000;MODE=PostgreSQL;LOG=2;TRACE_LEVEL_FILE=2;TRACE_LEVEL_SYSTEM_OUT=1",
//			"sa",
//			"",
//			"org.h2.Driver")
//		
//		return sql
//	}
	
	public static groovy.sql.Sql getExportSql()
	{	
		//**** otsa ****//
		groovy.sql.Sql sql = groovy.sql.Sql.newInstance(
			"jdbc:postgresql://ec2-54-83-199-115.compute-1.amazonaws.com/d9kq4fhomo114u?user=dkddxoaryojxoy&password=5EDOVxnd0uMKnwMPmKkfsNYxq4&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
			"",
			"",
			"org.postgresql.Driver")
		
		return sql
		
		//**** otsb ****//
//		groovy.sql.Sql sql = groovy.sql.Sql.newInstance(
//			"jdbc:postgresql://ec2-54-204-45-196.compute-1.amazonaws.com/d9rnmkei6la2gk?user=bxiyrquhwivxxu&password=d9eH3NciHb4jgOHawuTxyK6v53&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
//			"",
//			"",
//			"org.postgresql.Driver")
//		
//		return sql
	}
	
	public static groovy.sql.Sql getImportSql()
	{
		//**** otsa ****//
		groovy.sql.Sql sql = groovy.sql.Sql.newInstance(
				"jdbc:postgresql://ec2-54-83-199-115.compute-1.amazonaws.com/d9kq4fhomo114u?user=dkddxoaryojxoy&password=5EDOVxnd0uMKnwMPmKkfsNYxq4&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
				"",
				"",
				"org.postgresql.Driver")

		return sql
	}
	
}
