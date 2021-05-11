package io.shulie.amdb.adaptors.common;


/**
 * 可操作
 *
 * @author vincent
 */
public interface Operatable {

    /**
     * 模型时间戳
     *
     * @return
     */
    long timestamp();

    /**
     * 编号
     *
     * @return
     */
    String id();

    /**
     * 模型类型
     * c 创建
     * u 更新
     * d 删除
     * s 查询
     *
     * @return
     */
    char opType();

    public enum OpType {
        C, D, U, R;

        public char getValue() {
            return this.toString().charAt(0);
        }

        public static OpType valueOf(char value) {
            switch (value) {
                case 'C':
                    return OpType.C;
                case 'D':
                    return OpType.D;
                case 'R':
                    return OpType.R;
                case 'U':
                    return OpType.U;
            }
            return null;
        }
    }
}
