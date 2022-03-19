use linshiweimis12;

CREATE VIEW linsw_VGrades AS
SELECT linsw_students12.lsw_Sno12,linsw_students12.lsw_Sname12,linsw_students12.lsw_Scredit12,
       linsw_course12.lsw_Cno12,linsw_course12.lsw_Cname12,linsw_course12.lsw_Cyear12,linsw_course12.lsw_Cterm12,
       linsw_grade12.lsw_grade12,linsw_grade12.lsw_Tno12,linsw_teacher12.lsw_Tname12,linsw_course12.lsw_Ccredit12
FROM linsw_students12,linsw_grade12,linsw_course12,linsw_teacher12
WHERE linsw_students12.lsw_Sno12=linsw_grade12.lsw_Sno12
  AND linsw_course12.lsw_Cno12=linsw_grade12.lsw_CourseNo12
  AND linsw_teacher12.lsw_Tno12=linsw_grade12.lsw_Tno12;

CREATE VIEW linsw_vavg
AS
select linsw_grade12.lsw_Tno12,linsw_grade12.lsw_CourseNo12,linsw_course12.lsw_Cname12,avg(linsw_grade12.lsw_grade12) AS avg_Tno_Cno,COUNT(linsw_grade12.lsw_grade12) AS numberOfCourses
from linsw_grade12,linsw_course12
where linsw_course12.lsw_Cno12 = linsw_grade12.lsw_CourseNo12
Group by linsw_grade12.lsw_Tno12,linsw_grade12.lsw_CourseNo12;

CREATE VIEW linsw_VStudentsInfo AS
SELECT linsw_students12.lsw_Sno12,linsw_students12.lsw_Sname12,linsw_students12.lsw_Ssex12,
       linsw_students12.lsw_Sage12,linsw_students12.lsw_Shome12,linsw_students12.lsw_Scredit12,
       linsw_students12.lsw_SenterDate12,linsw_students12.lsw_Spassword12,linsw_class12.lsw_ClassNo12,linsw_class12.lsw_ClassName12,
       linsw_major12.lsw_Mno12,linsw_major12.lsw_Mname12
FROM linsw_students12,linsw_class12,linsw_major12
WHERE linsw_students12.lsw_Cno12=linsw_class12.lsw_ClassNo12
  AND linsw_class12.lsw_majorNo12=linsw_major12.lsw_Mno12 and linsw_students12.lsw_Sdelete12 = 0;


CREATE VIEW linsw_VTeaching AS
SELECT linsw_teacher12.lsw_Tno12,linsw_teacher12.lsw_Tname12,
       linsw_course12.lsw_Cno12,linsw_course12.lsw_Cname12,linsw_course12.lsw_Cway12,
       linsw_course12.lsw_Ccredit12,linsw_vavg.avg_Tno_Cno
FROM linsw_course12,linsw_teacher12,linsw_grade12,linsw_vavg
WHERE linsw_course12.lsw_Cno12=linsw_grade12.lsw_CourseNo12
  AND linsw_teacher12.lsw_Tno12=linsw_grade12.lsw_Tno12
  AND linsw_vavg.lsw_Tno12=linsw_teacher12.lsw_Tno12
  AND linsw_vavg.lsw_CourseNo12=linsw_course12.lsw_Cno12
Group BY linsw_teacher12.lsw_Tno12,linsw_course12.lsw_Cno12;


CREATE VIEW linsw_VCno_Avg(Cno,courseName,AvgScore)
AS
SELECT linsw_grade12.lsw_CourseNo12,linsw_course12.lsw_Cname12,AVG(linsw_grade12.lsw_grade12)
FROM linsw_grade12,linsw_course12
WHERE linsw_grade12.lsw_CourseNo12=linsw_course12.lsw_Cno12
GROUP BY linsw_grade12.lsw_CourseNo12;


CREATE VIEW linsw_Vcourse AS
SELECT linsw_vavg.lsw_Tno12,linsw_teacher12.lsw_Tname12, linsw_vavg.lsw_CourseNo12,linsw_vavg.lsw_Cname12,linsw_course12.lsw_Cyear12
        ,linsw_course12.lsw_Cterm12,linsw_course12.lsw_Cperiod12,linsw_course12.lsw_Cway12,
       linsw_course12.lsw_Ccredit12
        ,linsw_vavg.avg_Tno_Cno,
       linsw_vcno_avg.AvgScore
FROM linsw_vavg,linsw_vcno_avg,linsw_course12,linsw_teacher12
WHERE linsw_vavg.lsw_CourseNo12=linsw_vcno_avg.Cno and linsw_teacher12.lsw_Tno12 = linsw_vavg.lsw_Tno12 and linsw_vavg.lsw_CourseNo12 = linsw_course12.lsw_Cno12

