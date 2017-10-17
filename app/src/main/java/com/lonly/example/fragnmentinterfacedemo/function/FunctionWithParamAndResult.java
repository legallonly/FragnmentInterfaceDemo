package com.lonly.example.fragnmentinterfacedemo.function;

/**
 * Created by lonly on 2017/10/16.
 */

public abstract class FunctionWithParamAndResult<Result,Param> extends Function{
    public FunctionWithParamAndResult(String functionName){
        super(functionName);
    }
    public abstract Result function(Param pram);
}
