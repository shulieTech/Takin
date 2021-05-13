package io.shulie.tro.common.beans.page;

import java.io.Serializable;

/**
 * @author hezhongqi
 */
public class PagingDevice  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 每页条数
     */
    private int pageSize = 20;

    /**
     * 当前页码
     */
    private int current = 0;

    public int getOffset() {
        return Math.max(getCurrentPage() * pageSize, 0);
    }

    public int getCurrentPage() {
        return current;
    }

    public void setCurrentPage(Integer currentPage) {
        currentPage = currentPage == null ? 0 : currentPage;
        this.current = currentPage < 0 ? 0 : currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = (pageSize == null) ? getPageSize() : pageSize;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
