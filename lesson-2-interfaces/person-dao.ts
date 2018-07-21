import { Person } from './../leesson-1-classes/person';
import { DaoInterface } from './dao.interface';
export class PersonDao implements DaoInterface{

            
    tableName: String;

    insert(person: Person): boolean{
        console.log('Inserting .... ', person.toString());
        return true;
    }

    update(person: Person): boolean{
        return true;
    }
    
    delete(id: number):boolean{
        return true;
    }

    find(id: number): Person{
        return null;
    }

    findAll():[Person]{
        return [new Person('Stark')];
    }


}