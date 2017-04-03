package old.school;

/**
 * Created by slavik on 30.10.16.
 */
public abstract class Man {
        protected String name;
        protected int age;

        protected Man(String name){
            this.name = name;
        }
        protected Man(){

        }

        public void setName(String x){
            name = x;

        }

        public String getName(){
            return name;
        }

        public void setAge(int x){
            age = x;
        }

        public int getAge(){
            return age;
        }
}