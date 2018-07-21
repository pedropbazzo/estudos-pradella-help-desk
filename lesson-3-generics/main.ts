import { Student } from './../leesson-1-classes/student';
import { Dao } from './dao';
import { Person } from './../leesson-1-classes/person';
import { DaoInterface } from './dao.interface';


let dao: Dao<Person> = new Dao<Person>();
let person: Person = new Person('Utred');

dao.insert(person);


let dao2: Dao<Student> = new Dao<Student>();
let student: Student = new Student('Ragnar');

dao.insert(student);