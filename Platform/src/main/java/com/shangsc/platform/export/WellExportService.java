package com.shangsc.platform.export;

import com.shangsc.platform.model.Well;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ssc
 * @Date 2017/8/28 16:55
 * @Desc 用途：
 */
public class WellExportService extends ExportBaseService {
    
    private static final String FILE_NAME = "水井信息导出";

    /*
      水井编号	水井名称	单位编号	单位名称	所属节水办	所属区县	乡	村	水井地址	成井时间（年）
      井深（米）	地下水埋深（米）	是否为单位自备井	井口井管内径(毫米)	井壁管材料	水源类型	应用状况	是否已配套机电设备
      是否已安装水量计量设施	水泵型号	水量计量设施类型	是否为规模以上地下水水源地的水井	所在地貌类型区	所取用地下水的类型
      所在水资源三级区名称及编码	水源类型	主要取水用途及效益	取水量确定方法	是否已办理取水许可证	取水许可证编号	年许可取水量(万立方米)
     */
    public String export(List<Well> wells) {

        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "水井编号",
                "水井名称",
                "单位编号",
                "单位名称",
                "所属节水办",
                "所属区县",
                "乡",
                "村",
                "水井地址",
                "成井时间（年）",
                "井深（米）",
                "地下水埋深（米）",
                "是否为单位自备井",
                "井口井管内径（毫米）",
                "井壁管材料",
                "水源类型",
                "应用状况",
                "是否已配套机电设备",
                "是否已安装水量计量设施",
                "水泵型号",
                "水量计量设施类型",
                "是否为规模以上地下水水源地的水井",
                "所在地貌类型区",
                "所取用地下水的类型",
                "所在水资源三级区名称及编码",
                "主要取水用途及效益",
                "取水量确定方法",
                "是否已办理取水许可证",
                "取水许可证编号",
                "年许可取水量（万立方米）"
        }));
        logger.info("水表信息导出条数为:{}", wells.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Well well : wells) {
            Object[] obj = new Object[] {
                    well.getWellNum(),
                    well.getName(),
                    well.getInnerCode(),
                    well.get("companyName"),
                    well.get("water_unit"),
                    well.get("county"),
                    well.get("streetName"),
                    well.getVillage(),
                    well.getAddress(),
                    well.getYear(),
                    well.getWellDepth(),
                    well.getGroundDepth(),
                    well.get("oneselfWellName"),
                    well.getInnerDiameter(),
                    well.getMaterial(),
                    well.getWatersType(),
                    well.getApplication(),
                    well.get("electromechanicsName"),
                    well.get("calculateWaterName"),
                    well.getPumpModel(),
                    well.get("calculateTypeName"),
                    well.get("aboveScaleName"),
                    well.get("geomorphicTypeName"),
                    well.get("groundTypeName"),
                    well.getNameCode(),
                    well.getUseEfficiency(),
                    well.getMethod(),
                    well.get("licenceName"),
                    well.getLicenceCode(),
                    well.getWaterWithdrawals()
            };
            objects.add(obj);
        }
        return super.export(FILE_NAME, listHeader, objects);
    }
}
