package io.shulie.amdb.adaptors.utils;

public class FlagUtil {

    /**
     * 修改进制位
     * @param flag
     * @param site
     * @param b
     * @return
     */
    public static Integer setFlag(int flag, int site, boolean b) {
        if ((flag & site) == site) {
            if (!b) {
                return flag ^ site;
            }
        }else{
            if(b){
                return flag|site;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        System.out.println(setFlag(0,2,true));
        System.out.println(setFlag(0,2,false));
        System.out.println(setFlag(3,2,true));
        System.out.println(setFlag(3,2,false));
    }
}
