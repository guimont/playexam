# --- Sample dataset

# --- !Ups

insert into "users" values('admin@orsyp.com','admin','Orsdev2013');

insert into "tests" values (1,'Java france',7,30);

insert into "questions" values (1,1, 1, '1.	Which declaration of the main method below would allow a class to be started as a standalone program. Select the one correct answer.',false);
insert into "answers" values (1, 1, 'A.	public static int main(char args[]',true);
insert into "answers" values (2, 1, 'B.	public static void main(String args[])',true);
insert into "answers" values (3, 1, 'C.	public static void MAIN(String args[])',true);
insert into "answers" values (4, 1, 'D.	public static void main(String args)',true);
insert into "answers" values (5, 1, 'E.	public static void main(char args[])',true);
insert into "responses" values (1,1,1,true,'B');


insert into "questions" values ( 2,2, 1, '2.	What all gets printed when the following code is compiled and run? Select the three correct answers.', false);
insert into "parts" values (1,2, ' public class xyz {');
insert into "parts" values (2,2, '   public static void main(String args[]) { ');
insert into "parts" values (3,2, '      for(int i = 0;; i < 2;; i++) { ');
insert into "parts" values (4,2, '         for(int j = 2;; j>= 0;; j--) {');
insert into "parts" values (5,2, ' 	          if(i == j) break;;');
insert into "parts" values (6,2, '            System.out.println("i=" + i + " j="+j);;');
insert into "parts" values (7,2, '         }');
insert into "parts" values (8,2, '      }');
insert into "parts" values (9,2, '   }');
insert into "parts" values (10,2, '}');
insert into "answers" values (6, 2, 'A.	i=0 j=0',true);
insert into "answers" values (7, 2, 'B.	i=0 j=1',true);
insert into "answers" values (8, 2, 'C.	i=0 j=2',true);
insert into "answers" values (9, 2, 'D.	i=1 j=0',true);
insert into "answers" values (10, 2, 'E.	i=1 j=1',true);
insert into "answers" values (11, 2, 'F.	i=1 j=2',true);
insert into "answers" values (12, 2, 'G.	i=2 j=0',true);
insert into "answers" values (13, 2, 'H.	i=2 j=1',true);
insert into "answers" values (14, 2, 'I.	i=2 j=2',true);
insert into "responses" values (2,2,1,true,'B C F');


insert into "questions" values ( 3, 3, 1, '3.	What gets printed when the following code is compiled and run with the following command - java test 2 Select the one correct answer.',false);
insert into "parts" values (11,3, ' public class test { ');
insert into "parts" values (12,3, '   public static void main(String args[]) { ');
insert into "parts" values (13,3, '      Integer intObj=Integer.valueOf(args[args.length-1]);; ');
insert into "parts" values (14,3, '      int i = intObj.intValue();; ');
insert into "parts" values (15,3, '      if(args.length > 1)  ');
insert into "parts" values (16,3, '         System.out.println(i);; ');
insert into "parts" values (17,3, '      if(args.length > 0) ');
insert into "parts" values (18,3, '         System.out.println(i - 1);; ');
insert into "parts" values (19,3, '      else  ');
insert into "parts" values (20,3, '         System.out.println(i - 2);;');
insert into "parts" values (21,3, '   } ');
insert into "parts" values (22,3, ' }');
insert into "answers" values (31, 3, 'A.	test',true);
insert into "answers" values (32, 3, 'B.	test -1',true);
insert into "answers" values (33, 3, 'C.	0',true);
insert into "answers" values (34, 3, 'D.	1',true);
insert into "answers" values (35, 3, 'E.	2',true);
insert into "responses" values (3,3,1,true,'D');


insert into "questions" values ( 4,4, 1, '4.	Which of the following is a Java keyword. Select the four correct answers.', false);
insert into "answers" values (40, 4, 'A.	extern',true);
insert into "answers" values (41, 4, 'B.	synchronized',true);
insert into "answers" values (42, 4, 'C.	volatile',true);
insert into "answers" values (43, 4, 'D.	friend',true);
insert into "answers" values (44, 4, 'E.	friendly',true);
insert into "answers" values (45, 4, 'F.	transient',true);
insert into "answers" values (46, 4, 'G.	this',true);
insert into "answers" values (47, 4, 'H.	then',true);
insert into "responses" values (4,4,1,true,'B C F G');

insert into "questions" values ( 5,5, 1, '5.	Is the following statement true or false. The constructor of a class must not have a return type. ', false);
insert into "answers" values (50, 5, 'A.	true',true);
insert into "answers" values (51, 5, 'B.	false',true);
insert into "responses" values (5,5,1,true,'A');

insert into "questions" values ( 6,6, 1, '6.	What is the number of bytes used by Java primitive long. Select the one correct answer.', false);
insert into "answers" values (60, 6, 'A.	The number of bytes is compiler dependent.',true);
insert into "answers" values (61, 6, 'B.	2',true);
insert into "answers" values (62, 6, 'C.	4',true);
insert into "answers" values (63, 6, 'D.	8',true);
insert into "answers" values (64, 6, 'E.	64',true);
insert into "responses" values (6,6,1,true,'D');

insert into "questions" values ( 7,7, 1, '7.	Which of the following is correct? Select the two correct answers.', false);
insert into "answers" values (70, 7, 'A.	The native keyword indicates that the method is implemented in another language like C/C++. ',true);
insert into "answers" values (71, 7, 'B.	The only statements that can appear before an import statement in a Java file are comments. ',true);
insert into "answers" values (72, 7, 'C.	The method definitions inside interfaces are public and abstract. They cannot be private or protected. ',true);
insert into "answers" values (73, 7, 'D.	A class constructor may have public or protected keyword before them, nothing else. ',true);
insert into "responses" values (7,7,1,true,'A C');



insert into "tests" values (2,'JavaScript france',2,30);


insert into "candidates" values(1,'2000-01-01','anonymous','anonymous');
insert into "candidates" values(2,'2013-07-03','guillaume','monteilhet');
insert into "candidates" values(3,'2013-07-03','jerome','baudoux');

