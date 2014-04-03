/*----------------------------------------------------------------------------*/
/* This page of code was designed by Sage Thomas, Assembled by GLaDOS, not    */
/* distributed by Alex P, and hated on by the general populus. This code was  */
/* made for AcropolisBomber9001 that was also desiged and built by Sage Thomas*/
/* The dwarves have been planning a rebellion and have caused Sage to not goto*/
/* Lubbock of Great Texas. It will most likely cause the expedition to prove  */
/* fatal to the plebians that he had led.Alex if you are reading this and show*/
/* off what you have found, you're a damn plebian also. Have fun not being a  */
/* patricain, asshole.                                                        */
/*                                                                            */
/*                                        With much regards,                  */
/*                                         The Master Race Leader             */
/*                                          Sage Thomas                       */
/*                                                                            */
/* P.S. The master plan has been delayed, Alex. I am currently building the   */
/* microwave cannon needed to prevail in the war. Good Luck, jk even that     */
/* won't save you.                                                            */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;



public class AcropolisBomberFinalMain extends SimpleRobot {
    
    private Talon tal1 = new Talon(1);//left front 
    private Talon tal2 = new Talon(2);//left back 
    private Talon tal3 = new Talon(3);//right Front
    private Talon tal4 = new Talon(4);//Right back
    private Jaguar jag5 = new Jaguar(5);//Winch
    private Jaguar jag6 = new Jaguar(6);//Winch 
    private Jaguar leftLifter = new Jaguar(7); //left lifter thing
    private Jaguar rightLifter = new Jaguar(8); //right lifter thing
    private Relay triggerMotor = new Relay(1);
    private Relay intakeAngle = new Relay(2);
    
    private Joystick secondary = new Joystick(1);
    private Joystick Left = new Joystick(2);
    private Joystick Right = new Joystick(3);
    
    KinectStick leftArm = new KinectStick(1);
    KinectStick rightArm = new KinectStick(2);
    
    private DigitalInput STOP = new DigitalInput(1);
    
    private AnalogChannel inVal = new AnalogChannel(1);
    
    double speed = 1;
    double leftSpeed = 1;
    double gross = 0;
    double left = 0;
    double right =0;
    
    int winchPoint = 0;
    int fireCnt = 0;
    
    boolean isWinchDown = false; //tells wether the shooter arm is up or down
    boolean isArmLocked = false;
    boolean wasButtonPressed = false;
    boolean direction = true;
    boolean stop = false;
    boolean winchUp=false;
    boolean release = false;
    boolean triggerGo=false;
    boolean triggerForward=true;
    boolean triggerBackward=false;
    boolean triggerStop=false;
    boolean afterStop = false;
    boolean reset=false;
    boolean isDoneWinch = true;
    
    int lifterCnt = 0;
    double timerI=0;
    double timerII=0;
    double timerIII=0;
    double timerIV=0;
    double autonomousTimer=0;
    boolean countI=true;
    boolean switchI = false;
    double triggerSpeed;
    double directionChange=1;
    double voltage;
    
    int winchMoreTimer = 0;
    int hotNumber = 0;
    int coldNumber = 0;
    int nullNumber = 0;
    int goalPower = 0;
    
    public void autonomous() {
            autonomousTimer=0;
            boolean wasShot = false;
            while(isAutonomous()&&isEnabled()){
                left=leftArm.getY();
                right=rightArm.getY();
                autonomousTimer+=1;
//
                /*--------Hot o' Cold--------*/
                if(left>0){//Cold
                    if(autonomousTimer>700&&autonomousTimer<720){
                        triggerGo=true;
                        triggerForward=true;
                        wasShot = true;
                    }
                }else if(left<0){//Hot
                    if(autonomousTimer>320&&autonomousTimer<340){
                        triggerGo=true;
                        wasShot = true;
                        triggerForward=true;
                    }
                }
                /*--------Drive and Stop--------*/
                if(autonomousTimer<250){
                    tal1.set(.49);
                    tal2.set(.49);
                    tal3.set(-.4);
                    tal4.set(-.4);
                }else{
                    tal1.set(0);
                    tal2.set(0);
                    tal3.set(0);
                    tal4.set(0);
                }
                
            if(!wasShot&&autonomousTimer>700&&autonomousTimer<720){ //fail safe
                triggerGo=true;
                triggerForward=true;
            }    
                
            if(autonomousTimer>0 && autonomousTimer<20){
                intakeAngle.set(Relay.Value.kOn);
                intakeAngle.setDirection(Relay.Direction.kForward);
                leftLifter.set(-.3);
                rightLifter.set(.3);
            }else{
                intakeAngle.set(Relay.Value.kOff);
                leftLifter.set(0);
                rightLifter.set(0);
            }
            
                autoTrigger();
                Timer.delay(.01);
        }
    
    }

    
    public void operatorControl() {
        //println("Starting . . . ");
        triggerForward=true;
        while (isOperatorControl() && isEnabled()) {
            //println(String.valueOf(STOP.get()));
            stop=STOP.get();
            
            /*--------Drive--------*/
            if(Right.getRawButton(2)){ //Slow mode
                speed=.4;
                leftSpeed=.5;
            }else{                    //Normal Speed if no button pressed
                speed=.9;
                leftSpeed=1;
            }
            if(direction){//Direction determines what end of the robot is the front
                left=Left.getY()*-leftSpeed;
                right=Right.getY()*speed;
            }else{
                right=Left.getY()*-leftSpeed;
                left=Right.getY()*speed;
            }
            if(Right.getRawButton(4)&&!switchI){//NOT the correct button
                if (direction){
                    direction = false;
                }else{
                    direction = true;
                }
                switchI=true;
            }else{
                switchI=false;
            }
            tal1.set(left);
            tal2.set(left);
            tal3.set(right);
            tal4.set(right);
            /*--------Winch--------*/
            if(winchUp){//if the winch is up
                jag5.set(1);
                jag6.set(1);
            }else if(!winchUp&&!release){//if winch is down and release is not running shut down the winch
                jag5.set(0);
                jag6.set(0);
            }
            if(stop&&!afterStop&&!isDoneWinch){//Says that winch is down
                if(winchMoreTimer == 10){
                   winchUp=false;
                    release=true;
                    afterStop=true; 
                    winchMoreTimer = 0;
                }else{
                    winchMoreTimer += 1;
                }
                
            }if(release&&!winchUp){//reverses for _ seconds
                if(timerI<5){
                    jag5.set(-.7);
                    jag6.set(-.7);
                }
                else{
                    release=false;
                    isDoneWinch = true;
                    timerI=0;
                }
                timerI+=1;
                println("reversing");
            }
            /*--------Trigger--------*/
            trigger();
           
           /*---------Intake--------*/
           if(Left.getRawButton(4)||secondary.getRawButton(4)){
               leftLifter.set(-.6);
               rightLifter.set(.6);
           }else if(Left.getRawButton(5)||secondary.getRawButton(5)){
               leftLifter.set(.6);
               rightLifter.set(-.6);
           }else{
               leftLifter.set(0);
               rightLifter.set(0);
           }
           
           /*---------Intake Raise/Lower--------*/
           if(secondary.getRawButton(7)){ //raise
               if (lifterCnt < 3){
                   intakeAngle.set(Relay.Value.kOn);
                   intakeAngle.setDirection(Relay.Direction.kForward);
               }else if(lifterCnt == 6){
                   lifterCnt = -1;
                   intakeAngle.set(Relay.Value.kOff);
               }else{
                   intakeAngle.set(Relay.Value.kOff);
               }
               lifterCnt += 1;
           }else if(secondary.getRawButton(6)){ //lower
                if (lifterCnt < 3){
                   intakeAngle.set(Relay.Value.kOn);
                   intakeAngle.setDirection(Relay.Direction.kReverse);
                }else if(lifterCnt == 6){
                   lifterCnt = -1;
                   intakeAngle.set(Relay.Value.kOff);
                }else{
                   intakeAngle.set(Relay.Value.kOff);
                }
                lifterCnt += 1;
           }else{
               lifterCnt = 0;
               intakeAngle.set(Relay.Value.kOff);
           }
           
            Timer.delay(.01);
            DriverStationLCD.getInstance().updateLCD(); //updates the screen after all of the logs have bheen printed
            
            
            
        }
}
    
    public void println (String text){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6,5,text); //Prints "Hello World"
        //updates the screen
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public void trigger(){
        if(Left.getRawButton(1) || Right.getRawButton(1)){
            triggerGo=true;
            triggerForward=true;
        }else{
            triggerMotor.set(Relay.Value.kOff);
        }
        if(triggerGo){
            if(triggerForward){
                if(timerII<30){
                    triggerMotor.set(Relay.Value.kOn);//Hayden fixed the code
                    triggerMotor.setDirection(Relay.Direction.kForward);
                }
                else{
                    timerII=0;
                    triggerForward=false;
                    triggerBackward=true;
                }
//                    if(triggerButton.get()){
//                        triggerForward=false;
//                        triggerBackward=true;
//                    }
                timerII+=1;
            }else if(triggerBackward){
                if(timerII<30){
                    triggerMotor.set(Relay.Value.kOn);//Hayden fixed the code
                    triggerMotor.setDirection(Relay.Direction.kReverse);
                }else{
                    triggerStop=true;
                    triggerBackward=false;
                }
                timerII=timerII+1;  
            }else if(triggerStop){
                triggerMotor.set(Relay.Value.kOff);//Hayden fixed the code
                triggerStop=false;
                triggerGo=false;
                timerII=0;
                reset=true;
            }
        }
           /*----- Winch Reset----*/
           if(Left.getRawButton(3)||Right.getRawButton(3)){
               winchUp=true;
               afterStop=false;
               isDoneWinch=false;
           }
    }
    public void autoTrigger(){
        if(triggerGo){
            if(triggerForward){
                if(timerII<30){
                    triggerMotor.set(Relay.Value.kOn);//Hayden fixed the code
                    triggerMotor.setDirection(Relay.Direction.kForward);
                }
                else{
                    timerII=0;
                    triggerForward=false;
                    triggerBackward=true;
                }
//                    if(triggerButton.get()){
//                        triggerForward=false;
//                        triggerBackward=true;
//                    }
                timerII+=1;
            }else if(triggerBackward){
                if(timerII<30){
                    triggerMotor.set(Relay.Value.kOn);//Hayden fixed the code
                    triggerMotor.setDirection(Relay.Direction.kReverse);
                }else{
                    triggerStop=true;
                    triggerBackward=false;
                }
                timerII=timerII+1;  
            }else if(triggerStop){
                triggerMotor.set(Relay.Value.kOff);//Hayden fixed the code
                triggerStop=false;
                triggerGo=false;
                timerII=0;
                reset=true;
            }
        }
    }
}
    
  

