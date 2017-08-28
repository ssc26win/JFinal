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
        "单位名称", name: "companyName"
        "单位编号", name: "inner_code"
        "水井编号", name: "well_num"
        "水井名称", name: "name"
        "乡", name: "township"
        "村", name: "village"
        "水井地址", name: "address"
        "成井时间（年）", name: "start_date"
        "井深（米）", name: "well_depth"
        "地下水埋深（米）", name: "ground_depth"
        "是否为单位自备井", name: "oneselfWellName"
        "井口井管内径（毫米）", name: "inner_diameter"
        "井壁管材料", name: "material"
        "应用状况", name: "application"
        "水源类型", name: "watersTypeName"
        "是否已配套机电设备", name: "electromechanicsName"
        "是否已安装水量计量设施", name: "calculateWaterName"
        "水泵型号", name: "pump_model"
        "水量计量设施类型", name: "calculate_type"
        "是否为规模以上地下水水源地的水井", name: "above_scale"
        "所在地貌类型区", name: "geomorphic_type"
        "所取用地下水的类型", name: "ground_type"
        "所在水资源三级区名称及编码", name: "name_code"
        "主要取水用途及效益", name: "use_efficiency"
        "取水量确定方法", name: "method"
        "是否已办理取水许可证", name: "licenceName"
        "取水许可证编号", name: "licence_code"
        "年许可取水量（万立方米）", name: "water_withdrawals"
     */
    public String export(List<Well> wells) {

        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位名称",
                "单位编号",
                "水井编号",
                "水井名称",
                "乡",
                "村",
                "水井地址",
                "成井时间（年）",
                "井深（米）",
                "地下水埋深（米）",
                "是否为单位自备井",
                "井口井管内径（毫米）",
                "井壁管材料",
                "应用状况",
                "水源类型",
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
                    well.get("companyName"),
                    well.getInnerCode(),
                    well.getWellNum(),
                    well.getName(),
                    well.getTownship(),
                    well.getVillage(),
                    well.getAddress(),
                    well.getStartDate(),
                    well.getWellDepth(),
                    well.getGroundDepth(),
                    well.get("oneselfWellName"),
                    well.getInnerDiameter(),
                    well.getMaterial(),
                    well.getApplication(),
                    well.get("watersTypeName"),
                    well.get("electromechanicsName"),
                    well.get("calculateWaterName"),
                    well.getPumpModel(),
                    well.getCalculateType(),
                    well.get("aboveScaleName"),
                    well.getGeomorphicType(),
                    well.getGroundType(),
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
