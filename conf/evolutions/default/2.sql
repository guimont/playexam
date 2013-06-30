# --- Sample dataset

# --- !Ups


insert into "questions" values (1, 'java exo 1', '1.	Which declaration of the main method below would allow a class to be started as a standalone program. Select the one correct answer.');
insert into "answers" values (1, 1, 'A.	public static int main(char args[]',true);
insert into "answers" values (2, 1, 'B.	public static void main(String args[])',true);
insert into "answers" values (3, 1, 'C.	public static void MAIN(String args[])',true);
insert into "answers" values (4, 1, 'D.	public static void main(String args)',true);
insert into "answers" values (5, 1, 'E.	public static void main(char args[])',true);



insert into "questions" values ( 2, 'java exo 1', '2.	What all gets printed when the following code is compiled and run? Select the three correct answers.');
insert into "parts" values (1,2, ' public class xyz {');
insert into "parts" values (2,2, '   public static void main(String args[]) { ');
insert into "parts" values (3,2, '      for(int i = 0;; i < 2;; i++) { ');
insert into "parts" values (4,2, '         for(int j = 2;; j>= 0;; j--) {');
insert into "parts" values (5,2, ' 				if(i == j) break;;');
insert into "parts" values (6,2, '				System.out.println("i=" + i + " j="+j);;');
insert into "parts" values (7,2, '			}');
insert into "parts" values (8,2, '		}');
insert into "parts" values (9,2, '	}');
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


insert into "questions" values ( 3, 'java exo 1', '3.	write a qsort hanoi programm');
insert into "answers" values (15, 3, 'A.	empty',false);