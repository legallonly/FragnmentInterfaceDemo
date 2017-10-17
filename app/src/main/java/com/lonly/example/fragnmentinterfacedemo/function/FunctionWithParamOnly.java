package com.lonly.example.fragnmentinterfacedemo.function;

/**
 * Created by lonly on 2017/10/16.
 */

public abstract class FunctionWithParamOnly<Param> extends Function{
    public FunctionWithParamOnly(String functionName){
        super(functionName);
    }
    public abstract void function(Param pram);
}
