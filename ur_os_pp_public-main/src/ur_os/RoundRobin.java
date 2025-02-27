/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean singlequeue;
    
    RoundRobin(OS os){
        super(os);
        q = 5;
        cont=0;
        singlequeue = true;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean singlequeue){
        this(os);
        this.q = q;
        this.singlequeue = singlequeue;
    }
    

    
    void resetCounter(){
        cont=0;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if(cpuEmpty){
            //Case 1
            if(processes.isEmpty()){
                //Do Nothing
                cont=0;
            }else{
                //Case 2
                Process p = processes.get(0);
                processes.remove();
                cont = 0; //Reset the counter
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            }
        }else{
    
            cont++;
            if(cont == q){
                 if(processes.isEmpty()){
                     os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
                     if(!processes.isEmpty()){ //If the process returned to the RQ, then it must return to the CPU. If it is a MFQ, then the process may or may not return to this queue
                         Process p = processes.get(0);
                         processes.remove();
                         cont = 0; //Reset the counter
                         os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
                     }
                 }else{
                     Process p = processes.get(0);
                     processes.remove();
                     os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, p);
                 }
                 cont = 0;
             }
            
        }    
        
        
    }
    
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
