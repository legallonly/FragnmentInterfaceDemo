# FragnmentInterfaceDemo
Fragment之间通讯的万能接口框架

此项目实现了多个Fragment之间的数据传输通讯，以宿主Activty作为通讯桥梁，通过一个Fragment的基类BaseFragment与宿主Activity进行接口绑定，在宿主Activty中调用setFuctionsForFragment()实现接口方法。

BaseFragment与宿主Activity进行接口绑定：
```java
@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            mBaseActivity = (MainActivity) context;
            mBaseActivity.setFuctionsForFragment(getTag());
        }
    }
   ```
    
宿主Activty中调用setFuctionsForFragment()实现接口方法：  
 
```java
    public void setFuctionsForFragment(String tag){
         FragmentManager fm = getSupportFragmentManager();
         BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);
         FunctionManager functionManager = FunctionManager.getInstance();
         fragment.setmFunctionManager(functionManager.addFunction(new FunctionNoParamNoResault(Fragment1.INTERFACE) {
             @Override
             public void function() {
                 //接口中的方法回调
                 Toast.makeText(MainActivity.this,"成功调用无参无返回值的接口",Toast.LENGTH_SHORT).show();
             }
         }).addFunction(new FunctionWithResultOnly<String>(Fragment2.INTERFACE_RESULT) {
             @Override
             public String function() {
                 //接口中的方法回调
                 return "I Love U";
             }
         }).addFunction(new FunctionWithParamAndResult<String,String>(Fragment3.INTERFACE_PARAM_RESULT) {
             @Override
             public String function(String pram) {
                 //接口中的方法回调
                 return pram;
             }
         }).addFunction(new FunctionWithParamOnly<String>(Fragment4.INTERFACE_PARAM) {@Override
             public void function(String pram) {
                 //接口中的方法回调
                 Toast.makeText(MainActivity.this,"成功调用有参无返回值的接口：" + pram,Toast.LENGTH_SHORT).show();
             }
         }));
    }
```
具体说明请参考我的博客文章：http://blog.csdn.net/u012956630/article/details/78257145
