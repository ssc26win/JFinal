/* 修复水表数量 */
update t_company tc set tc.remotemeter_count=(select count(1) from t_water_meter twm where twm.inner_code=tc.inner_code)
where tc.inner_code is not null and tc.inner_code<>'';