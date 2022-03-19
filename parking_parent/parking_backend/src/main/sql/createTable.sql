use qiulinglongmis12;
-- 管理员表
CREATE TABLE `qiull_admin12` (
`qll_Ano12` VARCHAR(50) NOT NULL COMMENT '管理员编号',
`qll_Apassword12` VARCHAR(50) NULL DEFAULT NULL COMMENT '密码',
PRIMARY KEY (`qll_Ano12`)
)COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;
# 专业表
CREATE TABLE `qiull_major12` (
                                 `qll_Mno12` VARCHAR(50) NOT NULL COMMENT '专业编号',
                                 `qll_Mname12` VARCHAR(50) NOT NULL COMMENT '专业名',
                                 PRIMARY KEY (`qll_Mno12`)
)COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;
# 班级表
CREATE TABLE `qiull_class12` (
	`qll_ClassNo12` VARCHAR(50) NOT NULL COMMENT '班级编号' ,
	`qll_ClassName12` VARCHAR(50) NULL DEFAULT NULL COMMENT '班级名' ,
	`qll_majorNo12` VARCHAR(50) NOT NULL COMMENT '专业编号' ,
	PRIMARY KEY (`qll_ClassNo12`) ,
	INDEX `FK_qiull_class12_qiull_major12` (`qll_majorNo12`) ,
	CONSTRAINT `FK_qiull_class12_qiull_major12` FOREIGN KEY (`qll_majorNo12`) REFERENCES `qiulinglongmis12`.qiull_major12 (`qll_Mno12`) ON UPDATE NO ACTION ON DELETE NO ACTION
)COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;

#课程表
CREATE TABLE `qiull_course12` (
`qll_Cno12` VARCHAR(50) NOT NULL COMMENT '课程编号' ,
`qll_Cname12` VARCHAR(50) NULL DEFAULT NULL COMMENT '开课名称' ,
`qll_Cyear12` VARCHAR(50) NULL DEFAULT NULL COMMENT '开课学年' ,
`qll_Cterm12` VARCHAR(50) NULL DEFAULT NULL COMMENT '开课学期' ,
`qll_Cperiod12` INT(11) NULL DEFAULT NULL COMMENT '学时',
`qll_Cway12` VARCHAR(50) NULL DEFAULT NULL COMMENT '考察方式' ,
`qll_Ccredit12` DOUBLE(20,2) NULL DEFAULT NULL COMMENT '学分',
PRIMARY KEY (`qll_Cno12`) USING BTREE
)COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;


# 成绩表
CREATE TABLE `qiull_grade12` (
    `qll_CourseNo12` VARCHAR(50) NOT NULL COMMENT '课程编号',
    `qll_Sno12` VARCHAR(50) NOT NULL COMMENT '学生编号',
    `qll_grade12` INT(3) NULL DEFAULT NULL COMMENT '成绩',
    `qll_Tno12` VARCHAR(50) NULL DEFAULT NULL COMMENT '教师编号',
    PRIMARY KEY (`qll_CourseNo12`, `qll_Sno12`)
) COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;



#学生表
CREATE TABLE `qiull_students12` (
`qll_Sno12` VARCHAR(50) NOT NULL COMMENT '学号' ,
`qll_Sname12` VARCHAR(50) NULL DEFAULT NULL COMMENT '姓名' ,
`qll_Ssex12` CHAR(10) NULL DEFAULT NULL COMMENT '性别' ,
`qll_Sage12` INT(20) NULL DEFAULT NULL COMMENT '年龄',
`qll_Shome12` VARCHAR(50) NULL DEFAULT NULL COMMENT '生源所在地' ,
`qll_Scredit12` DOUBLE(20,2) NULL DEFAULT NULL COMMENT '已修学分',
`qll_SenterDate12` DATE NULL DEFAULT NULL COMMENT '入学年份',
`qll_Spassword12` VARCHAR(20) NULL DEFAULT NULL COMMENT '学生登入密码' ,
`qll_Cno12` VARCHAR(50) NULL DEFAULT NULL COMMENT '班级编号',
`qll_Sdelete12` INT(1) NULL DEFAULT NULL default 0 COMMENT '删除标记' ,
PRIMARY KEY (`qll_Sno12`),
INDEX `FK_qiull_students12_qiull_course12` (`qll_Cno12`),
CONSTRAINT `FK_qiull_students12_qiull_class12` FOREIGN KEY (`qll_Cno12`) REFERENCES qiulinglongmis12.qiull_class12 (`qll_ClassNo12`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;

CREATE TABLE `qiull_teacher12` (
`qll_Tno12` VARCHAR(50) NOT NULL COMMENT '教师编号' ,
`qll_Tname12` VARCHAR(50) NULL DEFAULT NULL COMMENT '姓名' ,
`qll_Tsex12` VARCHAR(10) NULL DEFAULT NULL COMMENT '性别' ,
`qll_Tage12` VARCHAR(20) NULL DEFAULT NULL COMMENT '年龄' ,
`qll_Ttitle12` VARCHAR(50) NULL DEFAULT NULL COMMENT '职称' ,
`qll_Tphone12` VARCHAR(50) NULL DEFAULT NULL COMMENT '联系电话' ,
`qll_Tpassword12` VARCHAR(32) NULL DEFAULT NULL COMMENT '教师登入密码',
PRIMARY KEY (`qll_Tno12`)
) COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;



CREATE TABLE `qiull_class_course12` (
`qll_ClassNo12` VARCHAR(50) NOT NULL COMMENT '班级编号',
`qll_CourseNo12` VARCHAR(50) NOT NULL COMMENT '课程编号',
`qll_TeacherNo12` VARCHAR(50) NOT NULL COMMENT '老师编号',
PRIMARY KEY (`qll_ClassNo12`, `qll_CourseNo12`, `qll_TeacherNo12`)
)COLLATE='utf8mb4_0900_ai_ci' ENGINE=InnoDB;

