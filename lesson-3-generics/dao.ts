import { Person } from './../leesson-1-classes/person';
import { DaoInterface } from './dao.interface';
export class Dao<T> implements DaoInterface<T>{

            
    tableName: String;

    insert(object: T): boolean{
        console.log('Inserting .... ');
        return true;
    }

    update(object: T): boolean{
        return true;
    }
    
    delete(id: number):boolean{
        return true;
    }

    find(id: number): T{
        return null;
    }

    findAll():[T]{
        return [null];
    }


}