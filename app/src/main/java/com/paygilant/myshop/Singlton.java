package com.paygilant.myshop;



public class Singlton {
    private static Singlton mInstance = null;





    private  Boolean isReg;



    public static Singlton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singlton();
        }
        return mInstance;
    }

    private Singlton(){
        isReg = false;
    }



    public Boolean isReg() {
        return isReg;
    }

    public void setReg(Boolean reg) {
        isReg = reg;
    }

}

