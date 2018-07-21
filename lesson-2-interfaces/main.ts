import { PersonDao } from './person-dao';
import { DaoInterface } from './dao.interface';
import { Person } from '../leesson-1-classes/person';


let personDao: DaoInterface = new PersonDao();
let person: Person = new Person('Stark');

personDao.insert(person);