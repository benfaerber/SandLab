package sand.controller;

import sand.model.SandLab;

public class SandRunner
{
   public static void main(String [] args)
   {
      SandLab app = new SandLab(50,50); 
      app.run();
   }
  
}