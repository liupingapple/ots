databaseChangeLog = {

	changeSet(author: "taliu (generated)", id: "1454429042618-1") {
		createTable(tableName: "admin_user") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "contact_type", type: "VARCHAR(255)")

			column(name: "contanctid", type: "VARCHAR(255)")

			column(name: "email", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "full_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "password", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "phone", type: "VARCHAR(255)")

			column(name: "role", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "school_id", type: "BIGINT")

			column(name: "user_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-2") {
		createTable(tableName: "answer") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "content", type: "VARCHAR(255)")

			column(name: "correct", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "question_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "serial_num", type: "VARCHAR(255)")

			column(name: "answers_idx", type: "INT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-3") {
		createTable(tableName: "assignment") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "assigned_by", type: "VARCHAR(255)")

			column(name: "comment", type: "VARCHAR(255)")

			column(name: "difficulty_bar", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "due_date", type: "DATETIME")

			column(name: "evaluation_power", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "focus_power", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "lesson_id", type: "BIGINT")

			column(name: "max_associated_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "max_total_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "min_associated_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "min_total_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "number_of_questions_per_page", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "qr_code", type: "VARCHAR(255)")

			column(name: "qr_code_new_quizurl", type: "VARCHAR(255)")

			column(name: "question_limit", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "question_type", type: "VARCHAR(255)")

			column(name: "subject", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "target_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "time_limit", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_available_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_knowledge_points", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-4") {
		createTable(tableName: "assignment_assignment_template") {
			column(name: "assignment_templates_id", type: "BIGINT")

			column(name: "assignment_template_id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-5") {
		createTable(tableName: "assignment_status") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "assignment_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "available_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "comment", type: "VARCHAR(255)")

			column(name: "correct_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "coverage_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "covered_knowledg_points", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "finished_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "master_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "recent_check_date", type: "DATETIME")

			column(name: "relative_target_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(3)")

			column(name: "student_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "to_be_focused_knowledge_id", type: "BIGINT")

			column(name: "total_knowledge_points", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-6") {
		createTable(tableName: "assignment_status_knowledge_point") {
			column(name: "assignment_status_coveredkps_id", type: "BIGINT")

			column(name: "knowledge_point_id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-7") {
		createTable(tableName: "assignment_template") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "school_id", type: "BIGINT")

			column(name: "subject", type: "VARCHAR(7)")

			column(name: "template_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-8") {
		createTable(tableName: "assignment_template_knowledge_point") {
			column(name: "assignment_template_knowledge_points_id", type: "BIGINT")

			column(name: "knowledge_point_id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-9") {
		createTable(tableName: "batch_knowledge_point_op") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "end_index", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "extra_fields", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "operation", type: "VARCHAR(26)") {
				constraints(nullable: "false")
			}

			column(name: "start_index", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "start_node", type: "VARCHAR(255)")

			column(name: "traverse_depth", type: "INT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-10") {
		createTable(tableName: "batch_operation") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "batch_error", type: "VARCHAR(255)")

			column(name: "batch_operation", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "batch_result", type: "VARCHAR(255)")

			column(name: "column_selector", type: "VARCHAR(255)")

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "end_index", type: "INT")

			column(name: "start_index", type: "INT")

			column(name: "start_node", type: "VARCHAR(255)")

			column(name: "traverse_depth", type: "INT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-11") {
		createTable(tableName: "batch_question_op") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "end_index", type: "INT")

			column(name: "error_rate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "extra_fields", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "input_by_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "input_date", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "operation", type: "VARCHAR(35)") {
				constraints(nullable: "false")
			}

			column(name: "reviewed_by", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "source", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "start_index", type: "INT")

			column(name: "term", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(13)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-12") {
		createTable(tableName: "child_point") {
			column(name: "knowledge_Id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "child_Id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-13") {
		createTable(tableName: "end_user") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "active", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "birth_date", type: "DATETIME")

			column(name: "brief_comment", type: "VARCHAR(2000)")

			column(name: "date_created", type: "DATETIME")

			column(name: "email", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "expired_date", type: "DATETIME")

			column(name: "full_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "identification_num", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "last_access_date", type: "DATETIME")

			column(name: "login_date", type: "DATETIME")

			column(name: "password", type: "VARCHAR(60)") {
				constraints(nullable: "false")
			}

			column(name: "sex", type: "VARCHAR(1)")

			column(name: "telephone1", type: "VARCHAR(60)")

			column(name: "telephone2", type: "VARCHAR(60)")

			column(name: "user_name", type: "VARCHAR(60)") {
				constraints(nullable: "false")
			}

			column(name: "weixin_openid", type: "VARCHAR(100)")

			column(name: "role", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-14") {
		createTable(tableName: "instructed_record") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "content", type: "VARCHAR(255)")

			column(name: "eva_content", type: "VARCHAR(255)")

			column(name: "eva_level", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "from_date", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "student_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "teacher_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "to_date", type: "DATETIME") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-15") {
		createTable(tableName: "knowledge_check_point") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "correct_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "date_practiced", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "family_correct", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_correct", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_total", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_total", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "family_total_sum", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "is_focused", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "knowledge_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "quiz_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "recent_correct", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "recent_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "recent_total", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "student_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "total_questions", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_sum", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-16") {
		createTable(tableName: "knowledge_point") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "associated_question", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "content", type: "VARCHAR(1000)")

			column(name: "degree", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "failed_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_size", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "master_ratio", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "qr_code", type: "VARCHAR(255)")

			column(name: "qr_code_videourl", type: "VARCHAR(255)")

			column(name: "total_access_count", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "total_failed", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "total_question", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "valid_family_size", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-17") {
		createTable(tableName: "latest_knowledge_check_point") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "date_practiced", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "family_correct", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_correct", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_recent_total", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "family_total", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "family_total_sum", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "is_focused", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "knowledge_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "recent_correct", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "recent_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "recent_total", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "student_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "total_correct", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_questions", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-18") {
		createTable(tableName: "lesson") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "begin_date", type: "DATETIME")

			column(name: "brief_comment", type: "VARCHAR(1000)")

			column(name: "classroom", type: "VARCHAR(255)")

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "end_date", type: "DATETIME")

			column(name: "lecture_file", type: "VARCHAR(255)")

			column(name: "lecture_note", type: "VARCHAR(255)")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "terms", type: "VARCHAR(2)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-19") {
		createTable(tableName: "message") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "content", type: "VARCHAR(255)")

			column(name: "msg_date", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "msg_from_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "msg_to_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "parent_id", type: "BIGINT")

			column(name: "subject", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-20") {
		createTable(tableName: "parent_point") {
			column(name: "knowledge_Id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "parent_Id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-21") {
		createTable(tableName: "question") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "analysis", type: "VARCHAR(3000)")

			column(name: "audio", type: "MEDIUMBLOB")

			column(name: "audio_type", type: "VARCHAR(40)")

			column(name: "description", type: "VARCHAR(15000)")

			column(name: "difficulty_level", type: "DECIMAL(19,1)")

			column(name: "error_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "img", type: "MEDIUMBLOB")

			column(name: "img_type", type: "VARCHAR(40)")

			column(name: "input_by", type: "VARCHAR(40)")

			column(name: "input_date", type: "DATETIME")

			column(name: "instructions", type: "VARCHAR(255)")

			column(name: "parent_question_id", type: "BIGINT")

			column(name: "plain_text", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "qid", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "qr_code", type: "INT")

			column(name: "qr_code_videourl", type: "VARCHAR(255)")

			column(name: "reviewed_by", type: "VARCHAR(40)")

			column(name: "source", type: "VARCHAR(60)")

			column(name: "term", type: "VARCHAR(40)")

			column(name: "total_practiced", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_score", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(40)") {
				constraints(nullable: "false")
			}

			column(name: "weekly_score", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "weekly_tryout", type: "INT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-22") {
		createTable(tableName: "question_knowledge_points") {
			column(name: "knowledge_point_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "question_id", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-23") {
		createTable(tableName: "quiz") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "answered_date", type: "DATETIME")

			column(name: "assignment_id", type: "BIGINT")

			column(name: "correct_num", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "latest_correct_rate", type: "DECIMAL(19,2)")

			column(name: "latest_coverage", type: "DECIMAL(19,2)")

			column(name: "master_level", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "master_rate", type: "DECIMAL(19,2)")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "not_answered_num", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "review_comment", type: "VARCHAR(1000)")

			column(name: "score", type: "DECIMAL(19,1)")

			column(name: "status", type: "VARCHAR(4)") {
				constraints(nullable: "false")
			}

			column(name: "student_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "time_taken", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "to_be_focused_knowledge_id", type: "BIGINT")

			column(name: "total_answer_num", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_question_num", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(4)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-24") {
		createTable(tableName: "quiz_question_record") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "brief_comment", type: "VARCHAR(1000)")

			column(name: "chosen_for_id", type: "BIGINT")

			column(name: "coverage_rate", type: "DECIMAL(19,2)")

			column(name: "favorite_flag", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "question_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "quiz_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "stu_note", type: "VARCHAR(400)")

			column(name: "te_note", type: "VARCHAR(400)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-25") {
		createTable(tableName: "school") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "address", type: "VARCHAR(255)")

			column(name: "administrator_id", type: "BIGINT")

			column(name: "contact_person", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "email", type: "VARCHAR(255)")

			column(name: "level", type: "VARCHAR(4)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "president", type: "VARCHAR(255)")

			column(name: "telephone1", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "telephone2", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "web_site", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-26") {
		createTable(tableName: "stu_answer") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "correct", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "quiz_question_record_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ref_answer_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "user_input", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-27") {
		createTable(tableName: "student") {
			column(name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "covered_subjects", type: "VARCHAR(255)")

			column(name: "enable_leveling", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "evaluation_power", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "focus_power", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "home_addr", type: "VARCHAR(30)")

			column(name: "home_city", type: "VARCHAR(30)")

			column(name: "home_province", type: "VARCHAR(30)")

			column(name: "level", type: "VARCHAR(4)") {
				constraints(nullable: "false")
			}

			column(name: "master_level", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "max_associated_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "maxkpto_show", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "max_total_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "min_associated_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "min_total_questions_to_evaluate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "number_of_questions_per_page", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "parent_link", type: "VARCHAR(100)")

			column(name: "parents", type: "VARCHAR(80)")

			column(name: "practice_history", type: "VARCHAR(6000)")

			column(name: "practice_number", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "repeat_power", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "school_id", type: "BIGINT")

			column(name: "scores", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "target_correct_rate", type: "DECIMAL(19,2)") {
				constraints(nullable: "false")
			}

			column(name: "teacher_id", type: "BIGINT")

			column(name: "term", type: "VARCHAR(2)")

			column(name: "to_show_knowledge_graph", type: "BIT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-28") {
		createTable(tableName: "student_quiz_question_record") {
			column(name: "student_failed_records_id", type: "BIGINT")

			column(name: "quiz_question_record_id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-29") {
		createTable(tableName: "tclass") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "brief_comment", type: "VARCHAR(2000)")

			column(name: "class_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "in_charge_teacher_id", type: "BIGINT")

			column(name: "monitor_id", type: "BIGINT")

			column(name: "schedule", type: "VARCHAR(255)")

			column(name: "student_count", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "teaching_plan", type: "VARCHAR(255)")

			column(name: "total_error_rate", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "total_questions_practiced", type: "DECIMAL(19,2)")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-30") {
		createTable(tableName: "tclass_student") {
			column(name: "tclass_students_id", type: "BIGINT")

			column(name: "student_id", type: "BIGINT")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-31") {
		createTable(tableName: "teacher") {
			column(name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "covered_subjects", type: "VARCHAR(255)")

			column(name: "degree", type: "VARCHAR(7)")

			column(name: "entry_time", type: "DATETIME")

			column(name: "native_place", type: "VARCHAR(3)")

			column(name: "resident_addr", type: "VARCHAR(3)")

			column(name: "school_id", type: "BIGINT")

			column(name: "status", type: "VARCHAR(8)")

			column(name: "teacher_type", type: "VARCHAR(3)")

			column(name: "to_show_knowledge_graph", type: "BIT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-32") {
		createTable(tableName: "wei_xin") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "input4menu_click", type: "BIT") {
				constraints(nullable: "false")
			}

			column(name: "latest_menu_click", type: "VARCHAR(255)")

			column(name: "open_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-33") {
		addPrimaryKey(columnNames: "question_id, knowledge_point_id", tableName: "question_knowledge_points")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-82") {
		createIndex(indexName: "user_name", tableName: "admin_user", unique: "true") {
			column(name: "user_name")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-83") {
		createIndex(indexName: "template_name", tableName: "assignment_template", unique: "true") {
			column(name: "template_name")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-84") {
		createIndex(indexName: "user_name", tableName: "end_user", unique: "true") {
			column(name: "user_name")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-85") {
		createIndex(indexName: "weixin_openid", tableName: "end_user", unique: "true") {
			column(name: "weixin_openid")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-86") {
		createIndex(indexName: "datePracticed_idx", tableName: "knowledge_check_point", unique: "false") {
			column(name: "date_practiced")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-87") {
		createIndex(indexName: "recentCorrectRate_idx", tableName: "knowledge_check_point", unique: "false") {
			column(name: "recent_correct_rate")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-88") {
		createIndex(indexName: "name", tableName: "knowledge_point", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-89") {
		createIndex(indexName: "datePracticed_idx", tableName: "latest_knowledge_check_point", unique: "false") {
			column(name: "date_practiced")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-90") {
		createIndex(indexName: "recentCorrectRate_idx", tableName: "latest_knowledge_check_point", unique: "false") {
			column(name: "recent_correct_rate")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-91") {
		createIndex(indexName: "qid", tableName: "question", unique: "true") {
			column(name: "qid")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-92") {
		createIndex(indexName: "open_id", tableName: "wei_xin", unique: "true") {
			column(name: "open_id")
		}
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-34") {
		addForeignKeyConstraint(baseColumnNames: "school_id", baseTableName: "admin_user", baseTableSchemaName: "otsv2", constraintName: "FK29045ABBE1B9601A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "school", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-35") {
		addForeignKeyConstraint(baseColumnNames: "question_id", baseTableName: "answer", baseTableSchemaName: "otsv2", constraintName: "FKABCA3FBE26C7FADA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "question", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-36") {
		addForeignKeyConstraint(baseColumnNames: "lesson_id", baseTableName: "assignment", baseTableSchemaName: "otsv2", constraintName: "FK3D2B86CDE484D99A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "lesson", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-37") {
		addForeignKeyConstraint(baseColumnNames: "assignment_template_id", baseTableName: "assignment_assignment_template", baseTableSchemaName: "otsv2", constraintName: "FK3F92793A4AED11D5", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment_template", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-38") {
		addForeignKeyConstraint(baseColumnNames: "assignment_templates_id", baseTableName: "assignment_assignment_template", baseTableSchemaName: "otsv2", constraintName: "FK3F92793AEBC5F9C0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-39") {
		addForeignKeyConstraint(baseColumnNames: "assignment_id", baseTableName: "assignment_status", baseTableSchemaName: "otsv2", constraintName: "FK258075A458A9857A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-40") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "assignment_status", baseTableSchemaName: "otsv2", constraintName: "FK258075A43B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-41") {
		addForeignKeyConstraint(baseColumnNames: "to_be_focused_knowledge_id", baseTableName: "assignment_status", baseTableSchemaName: "otsv2", constraintName: "FK258075A4AF160F8E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-42") {
		addForeignKeyConstraint(baseColumnNames: "assignment_status_coveredkps_id", baseTableName: "assignment_status_knowledge_point", baseTableSchemaName: "otsv2", constraintName: "FK33694DF44BEBDA06", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment_status", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-43") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_point_id", baseTableName: "assignment_status_knowledge_point", baseTableSchemaName: "otsv2", constraintName: "FK33694DF4C658B75D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-44") {
		addForeignKeyConstraint(baseColumnNames: "school_id", baseTableName: "assignment_template", baseTableSchemaName: "otsv2", constraintName: "FK29DACDACE1B9601A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "school", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-45") {
		addForeignKeyConstraint(baseColumnNames: "assignment_template_knowledge_points_id", baseTableName: "assignment_template_knowledge_point", baseTableSchemaName: "otsv2", constraintName: "FK2D6295FC8BAC4DAA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment_template", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-46") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_point_id", baseTableName: "assignment_template_knowledge_point", baseTableSchemaName: "otsv2", constraintName: "FK2D6295FCC658B75D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-47") {
		addForeignKeyConstraint(baseColumnNames: "input_by_id", baseTableName: "batch_question_op", baseTableSchemaName: "otsv2", constraintName: "FK631E135529405228", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "admin_user", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-48") {
		addForeignKeyConstraint(baseColumnNames: "child_Id", baseTableName: "child_point", baseTableSchemaName: "otsv2", constraintName: "FKC7C168ADF53BE5B0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-49") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_Id", baseTableName: "child_point", baseTableSchemaName: "otsv2", constraintName: "FKC7C168ADFDD21F0E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-50") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "instructed_record", baseTableSchemaName: "otsv2", constraintName: "FK355713F73B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-51") {
		addForeignKeyConstraint(baseColumnNames: "teacher_id", baseTableName: "instructed_record", baseTableSchemaName: "otsv2", constraintName: "FK355713F74BED979A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-52") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_id", baseTableName: "knowledge_check_point", baseTableSchemaName: "otsv2", constraintName: "FK8EFFB158FDD21F0E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-53") {
		addForeignKeyConstraint(baseColumnNames: "quiz_id", baseTableName: "knowledge_check_point", baseTableSchemaName: "otsv2", constraintName: "FK8EFFB158F129207A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "quiz", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-54") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "knowledge_check_point", baseTableSchemaName: "otsv2", constraintName: "FK8EFFB1583B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-55") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_id", baseTableName: "latest_knowledge_check_point", baseTableSchemaName: "otsv2", constraintName: "FK9B3B12A0FDD21F0E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-56") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "latest_knowledge_check_point", baseTableSchemaName: "otsv2", constraintName: "FK9B3B12A03B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-57") {
		addForeignKeyConstraint(baseColumnNames: "msg_from_id", baseTableName: "message", baseTableSchemaName: "otsv2", constraintName: "FK38EB0007BC1ACB78", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "end_user", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-58") {
		addForeignKeyConstraint(baseColumnNames: "msg_to_id", baseTableName: "message", baseTableSchemaName: "otsv2", constraintName: "FK38EB000745B07F87", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "end_user", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-59") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_Id", baseTableName: "parent_point", baseTableSchemaName: "otsv2", constraintName: "FK53A319BBFDD21F0E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-60") {
		addForeignKeyConstraint(baseColumnNames: "parent_Id", baseTableName: "parent_point", baseTableSchemaName: "otsv2", constraintName: "FK53A319BBDB87F62", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-61") {
		addForeignKeyConstraint(baseColumnNames: "parent_question_id", baseTableName: "question", baseTableSchemaName: "otsv2", constraintName: "FKBA823BE69915B2C5", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "question", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-62") {
		addForeignKeyConstraint(baseColumnNames: "knowledge_point_id", baseTableName: "question_knowledge_points", baseTableSchemaName: "otsv2", constraintName: "FKA95FB6FDC658B75D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-63") {
		addForeignKeyConstraint(baseColumnNames: "question_id", baseTableName: "question_knowledge_points", baseTableSchemaName: "otsv2", constraintName: "FKA95FB6FD26C7FADA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "question", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-64") {
		addForeignKeyConstraint(baseColumnNames: "assignment_id", baseTableName: "quiz", baseTableSchemaName: "otsv2", constraintName: "FK35225558A9857A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "assignment", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-65") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "quiz", baseTableSchemaName: "otsv2", constraintName: "FK3522553B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-66") {
		addForeignKeyConstraint(baseColumnNames: "to_be_focused_knowledge_id", baseTableName: "quiz", baseTableSchemaName: "otsv2", constraintName: "FK352255AF160F8E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-67") {
		addForeignKeyConstraint(baseColumnNames: "chosen_for_id", baseTableName: "quiz_question_record", baseTableSchemaName: "otsv2", constraintName: "FK75D34CE0B2E0A850", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "knowledge_point", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-68") {
		addForeignKeyConstraint(baseColumnNames: "question_id", baseTableName: "quiz_question_record", baseTableSchemaName: "otsv2", constraintName: "FK75D34CE026C7FADA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "question", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-69") {
		addForeignKeyConstraint(baseColumnNames: "quiz_id", baseTableName: "quiz_question_record", baseTableSchemaName: "otsv2", constraintName: "FK75D34CE0F129207A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "quiz", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-70") {
		addForeignKeyConstraint(baseColumnNames: "administrator_id", baseTableName: "school", baseTableSchemaName: "otsv2", constraintName: "FKC9E15B741F0D44A7", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "admin_user", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-71") {
		addForeignKeyConstraint(baseColumnNames: "quiz_question_record_id", baseTableName: "stu_answer", baseTableSchemaName: "otsv2", constraintName: "FKB323DC2928818146", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "quiz_question_record", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-72") {
		addForeignKeyConstraint(baseColumnNames: "ref_answer_id", baseTableName: "stu_answer", baseTableSchemaName: "otsv2", constraintName: "FKB323DC295A4FDB2E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "answer", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-73") {
		addForeignKeyConstraint(baseColumnNames: "school_id", baseTableName: "student", baseTableSchemaName: "otsv2", constraintName: "FK8FFE823BE1B9601A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "school", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-74") {
		addForeignKeyConstraint(baseColumnNames: "teacher_id", baseTableName: "student", baseTableSchemaName: "otsv2", constraintName: "FK8FFE823B4BED979A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-75") {
		addForeignKeyConstraint(baseColumnNames: "quiz_question_record_id", baseTableName: "student_quiz_question_record", baseTableSchemaName: "otsv2", constraintName: "FK6EF286428818146", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "quiz_question_record", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-76") {
		addForeignKeyConstraint(baseColumnNames: "student_failed_records_id", baseTableName: "student_quiz_question_record", baseTableSchemaName: "otsv2", constraintName: "FK6EF28646BB87BD1", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-77") {
		addForeignKeyConstraint(baseColumnNames: "in_charge_teacher_id", baseTableName: "tclass", baseTableSchemaName: "otsv2", constraintName: "FKCB97D184552DA96B", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "teacher", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-78") {
		addForeignKeyConstraint(baseColumnNames: "monitor_id", baseTableName: "tclass", baseTableSchemaName: "otsv2", constraintName: "FKCB97D184E740497B", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-79") {
		addForeignKeyConstraint(baseColumnNames: "student_id", baseTableName: "tclass_student", baseTableSchemaName: "otsv2", constraintName: "FKB30E36803B524CFA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "student", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-80") {
		addForeignKeyConstraint(baseColumnNames: "tclass_students_id", baseTableName: "tclass_student", baseTableSchemaName: "otsv2", constraintName: "FKB30E368055DEA32B", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "tclass", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}

	changeSet(author: "taliu (generated)", id: "1454429042618-81") {
		addForeignKeyConstraint(baseColumnNames: "school_id", baseTableName: "teacher", baseTableSchemaName: "otsv2", constraintName: "FKAA31CBE2E1B9601A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "school", referencedTableSchemaName: "otsv2", referencesUniqueColumn: "false")
	}
}
