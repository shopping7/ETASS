package cn.shopping.ETASS.domain;

public class ArrayToString {

        public static String formArrayToString(String[] array) {
            String str="";
            for (int i = 0; i < array.length; i++) {
                //需要区分是不是最后一个
                if(i==array.length-1){
                    str+="\"" +array[i]+"\"";
                }else{
                    str+="\""+array[i]+"\""+",";
                }
            }
            return str;
        }

//    public static void main(String[] args) {
//        int[] array={1,2,3};
//        String result=formArrayToString(array);
//        System.out.println(result);
//    }

}
