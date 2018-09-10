/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.model;

import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseDictData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DictData extends BaseDictData<DictData> {

    public static final DictData dao = new DictData();

    public InvokeResult saveDictData(Integer id, String name, String remark,
                                     Integer seq, String value, Integer typeId) {
        DictData dictData = new DictData();
        dictData.setId(id);
        dictData.setName(name);
        dictData.setSeq(seq);
        dictData.setRemark(remark);
        dictData.setValue(value);
        dictData.setDictTypeId(typeId);
        dictData.setUpdateTime(DateUtils.formatDateToUnixTimestamp(new Date()));
        if (id != null) {
            dictData.update();
        } else {
            dictData.save();
        }
        return InvokeResult.success();
    }

    public Long insertDictData(String name, String remark,
                                       Integer seq, String value, Integer typeId) {
        DictData dictData = new DictData();
        dictData.setName(name);
        dictData.setSeq(seq);
        dictData.setRemark(remark);
        dictData.setValue(value);
        dictData.setDictTypeId(typeId);
        dictData.setUpdateTime(DateUtils.formatDateToUnixTimestamp(new Date()));
        dictData.save();

        return dictData.get("id");
    }

    public InvokeResult deleteData(Integer id) {
        this.deleteById(id);
        return InvokeResult.success();
    }

    public Map<String, Object> getDictMap(Integer typeId, String typeName) {
        if ((!(typeId != null && typeId > 0)) && StringUtils.isNotEmpty(typeName)) {
            DictType dictType = DictType.dao.findFirst("select * from dict_type where name='" + typeName + "'");
            if (dictType != null) {
                typeId = dictType.getId();
            }
        }
        Map<String, Object> allMap = new HashMap<String, Object>();
        if (typeId > 0L) {
            List<DictData> list = this.find("select value,name from dict_data where dict_type_id=" + typeId);
            if (CollectionUtils.isNotEmpty(list)) {
                for (DictData dictData : list) {
                    allMap.put(dictData.getValue(), dictData.getName());
                }
            }
        }
        return allMap;
    }

    public List<Map<String, Object>> getDictMapList(Integer typeId, String typeName) {
        if ((!(typeId != null && typeId > 0)) && StringUtils.isNotEmpty(typeName)) {
            DictType dictType = DictType.dao.findFirst("select * from dict_type where name='" + typeName + "'");
            typeId = dictType.getId();
        }
        List<DictData> list = this.find("select value,name from dict_data where dict_type_id=" + typeId);
        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictData dictData : list) {
                Map<String, Object> dictDataMap = new HashMap<String, Object>();
                dictDataMap.put("value", dictData.getValue());
                dictDataMap.put("name", dictData.getName());
                allList.add(dictDataMap);
            }
        }
        return allList;
    }

    public List<Map<String, Object>> getDictMapList(List<Integer> typeIds, String typeNames) {
        if ((CollectionUtils.isEmpty(typeIds)) && StringUtils.isNotEmpty(typeNames)) {
            List<DictType> dictTypes = DictType.dao.find("select * from dict_type where name in (" + typeNames + ")");
            Set<Integer> typeIdSet = new LinkedHashSet<>();
            for (DictType dictType : dictTypes) {
                typeIdSet.add(dictType.getId());
            }
            typeIds.addAll(typeIdSet);
        }
        List<DictData> list = this.find("select value,name from dict_data where dict_type_id in (" + StringUtils.join(typeIds, ",") + ")");
        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictData dictData : list) {
                Map<String, Object> dictDataMap = new HashMap<String, Object>();
                dictDataMap.put("value", dictData.getValue());
                dictDataMap.put("name", dictData.getName());
                allList.add(dictDataMap);
            }
        }
        return allList;
    }


    public Map<String, Integer> getDictNameMap(String typeName) {
        Integer typeId = null;
        if (StringUtils.isNotEmpty(typeName)) {
            DictType dictType = DictType.dao.findFirst("select * from dict_type where name='" + typeName + "'");
            typeId = dictType.getId();
        }
        List<DictData> list = this.find("select value,name from dict_data where dict_type_id=" + typeId);
        Map<String, Integer> all = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictData dictData : list) {
                all.put(StringUtils.trim(dictData.getName()), Integer.parseInt(StringUtils.trim(dictData.getValue())));

            }
        }
        return all;
    }

    public Map<Integer, String> getDictValMap(String typeName) {
        Integer typeId = null;
        if (StringUtils.isNotEmpty(typeName)) {
            DictType dictType = DictType.dao.findFirst("select * from dict_type where name='" + typeName + "'");
            typeId = dictType.getId();
        }
        List<DictData> list = this.find("select value,name from dict_data where dict_type_id=" + typeId);
        Map<Integer, String> all = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictData dictData : list) {
                all.put(Integer.parseInt(StringUtils.trim(dictData.getValue())), dictData.getName());

            }
        }
        return all;
    }

    public DictData getLatestDictData(String typeName) {
        Integer typeId = null;
        if (StringUtils.isNotEmpty(typeName)) {
            DictType dictType = DictType.dao.findFirst("select * from dict_type where name='" + typeName + "'");
            typeId = dictType.getId();
        }
        DictData first = this.findFirst("select * from dict_data where dict_type_id=" + typeId + " order by value desc limit 1");
        return first;
    }

}
