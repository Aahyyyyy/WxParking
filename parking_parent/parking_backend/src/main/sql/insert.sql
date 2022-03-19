use qiulinglongmis12;

# 管理员
insert into qiull_admin12 (qll_Ano12, qll_Apassword12) values ('admin',md5('123456'));

# 专业
insert into qiull_major12 (qll_Mno12, qll_Mname12) VALUES ('M0001','软件工程');
insert into qiull_major12 (qll_Mno12, qll_Mname12) VALUES ('M0002','计算机科学与技术');
insert into qiull_major12 (qll_Mno12, qll_Mname12) VALUES ('M0003','网络工程');
insert into qiull_major12 (qll_Mno12, qll_Mname12) VALUES ('M0004','数字媒体技术');
insert into qiull_major12 (qll_Mno12, qll_Mname12) VALUES ('M0005','大数据');

# 学生
insert into qiull_students12 (qll_Sno12, qll_Sname12, qll_Ssex12, qll_Sage12, qll_Shome12, qll_Scredit12, qll_SenterDate12, qll_Spassword12, qll_Cno12, qll_Sdelete12)
VALUES ('201906062501','小明','男',21,'浙江省杭州市',110.00,'2001-03-20',md5('123456'),'1901',0);
insert into qiull_students12 (qll_Sno12, qll_Sname12, qll_Ssex12, qll_Sage12, qll_Shome12, qll_Scredit12, qll_SenterDate12, qll_Spassword12, qll_Cno12, qll_Sdelete12)
VALUES ('201906062502','小红','女',19,'浙江省金华市',100.00,'2001-04-10',md5('123456'),'1901',0);
insert into qiull_students12 (qll_Sno12, qll_Sname12, qll_Ssex12, qll_Sage12, qll_Shome12, qll_Scredit12, qll_SenterDate12, qll_Spassword12, qll_Cno12, qll_Sdelete12)
VALUES ('201906062401','小华','男',22,'河北省衡水市',130.00,'2001-04-20',md5('123456'),'1901',0);
# 班级
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1801','软件工程1801','M0001');
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1901','计算机科学与技术1901','M0002');
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1902','网络工程1902','M0003');
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1903','数字媒体技术1903','M0004');
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1904','大数据1904','M0005');
insert into qiull_class12 (qll_ClassNo12, qll_ClassName12, qll_majorNo12) VALUES ('1905','软件工程1905','M0001');

# 课程
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0001','数据库','2021','下学期',48,'考试',3.00);
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0002','高数','2019','上学期',64,'考试',5.00);
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0003','大学物理','2020','下学期',48,'考试',3.00);
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0004','数据结构','2021','上学期',32,'考试',2.00);
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0005','大学英语','2020','下学期',64,'考试',5.00);
insert into qiull_course12 (qll_Cno12, qll_Cname12, qll_Cyear12, qll_Cterm12, qll_Cperiod12, qll_Cway12, qll_Ccredit12)
values ('C0006','计算机组成原理','2019','下学期',48,'考试',3.00);

#成绩
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0001','201906062501',95,'T0001');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0001','201906062502',60,'T0001');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0001','201906062401',85,'T0001');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0002','201906062501',85,'T0002');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0003','201906062501',90,'T0003');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0004','201906062502',92,'T0004');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0005','201906062502',84,'T0001');
insert into qiull_grade12 (qll_CourseNo12, qll_Sno12, qll_grade12, qll_Tno12) VALUES ('C0006','201906062502',98,'T0002');


# 教师
insert into qiull_teacher12 (qll_Tno12, qll_Tname12, qll_Tsex12, qll_Tage12, qll_Ttitle12, qll_Tphone12, qll_Tpassword12)
VALUES ('T0001','陆亿红','女',35,'副教授','13774733456',md5('123456'));
insert into qiull_teacher12 (qll_Tno12, qll_Tname12, qll_Tsex12, qll_Tage12, qll_Ttitle12, qll_Tphone12, qll_Tpassword12)
VALUES ('T0002','朱文忠','男',45,'讲师','13679876544',md5('123456'));
insert into qiull_teacher12 (qll_Tno12, qll_Tname12, qll_Tsex12, qll_Tage12, qll_Ttitle12, qll_Tphone12, qll_Tpassword12)
VALUES ('T0003','韩姗姗','女',25,'副教授','13643264264',md5('123456'));
insert into qiull_teacher12 (qll_Tno12, qll_Tname12, qll_Tsex12, qll_Tage12, qll_Ttitle12, qll_Tphone12, qll_Tpassword12)
VALUES ('T0004','管秋','女',35,'教授','15234654321',md5('123456'));

# 班级课程
insert into qiull_class_course12 (qll_ClassNo12, qll_CourseNo12, qll_TeacherNo12) VALUES ('1901','C0001','T0001');
insert into qiull_class_course12 (qll_ClassNo12, qll_CourseNo12, qll_TeacherNo12) VALUES ('1902','C0002','T0002');
insert into qiull_class_course12 (qll_ClassNo12, qll_CourseNo12, qll_TeacherNo12) VALUES ('1903','C0003','T0003');
insert into qiull_class_course12 (qll_ClassNo12, qll_CourseNo12, qll_TeacherNo12) VALUES ('1904','C0004','T0004');





