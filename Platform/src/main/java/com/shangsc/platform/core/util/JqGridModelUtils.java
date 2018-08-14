/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.core.util;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.model.JqGridModel;

import java.util.List;

/**
 * jdGrid分页Model转换工具
 */
public final class JqGridModelUtils {

    public static JqGridModel toJqGridView(Page pageInfo) {
        JqGridModel jqGridView = new JqGridModel();
        jqGridView.setPage(pageInfo.getPageNumber());
        jqGridView.setRecords(pageInfo.getTotalRow());
        jqGridView.setRows(pageInfo.getList());
        jqGridView.setTotal(pageInfo.getTotalPage());
        return jqGridView;
    }

    public static JqGridModel toJqGridView(Page pageInfo, List<?> changeList) {
        JqGridModel jqGridView = new JqGridModel();
        jqGridView.setPage(pageInfo.getPageNumber());
        jqGridView.setRecords(pageInfo.getTotalRow());
        jqGridView.setRows(changeList);
        jqGridView.setTotal(pageInfo.getTotalPage());
        return jqGridView;
    }
}
