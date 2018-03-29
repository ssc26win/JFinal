

select name,t.inner_code,sum(t.net_water) as total,'一月' as month from
(select * from t_actual_data where 1=1 and write_time >= '2018-01-01 00:00:00' and write_time < '2018-02-01 00:00:00' and inner_code in(100697,160051,160058,160059)) t
LEFT JOIN (select inner_code as ic,name from t_company) c on c.ic=t.inner_code
group by t.inner_code;

select name,t.inner_code,sum(t.net_water) as total,'二月' as month from
(select * from t_actual_data where 1=1 and write_time >= '2018-02-01 00:00:00' and write_time < '2018-03-01 00:00:00' and inner_code in(100697,160051,160058,160059)) t
LEFT JOIN (select inner_code as ic,name from t_company) c on c.ic=t.inner_code
group by t.inner_code;

select name,t.inner_code,sum(t.net_water) as total,'三月' as month from
(select * from t_actual_data where 1=1 and write_time >= '2018-03-01 00:00:00' and write_time < '2018-04-01 00:00:00' and inner_code in(100697,160051,160058,160059)) t
LEFT JOIN (select inner_code as ic,name from t_company) c on c.ic=t.inner_code
group by t.inner_code;




select * from t_actual_data where 1=1 and write_time >= '2018-03-01 00:00:00' and write_time < '2018-04-01 00:00:00' and inner_code in(100697);

 select tad.net_water,tc.name,tc.inner_code,tc.address,tc.water_unit,tc.county,tc.company_type,twm.waters_type,twm.meter_attr,twm.meter_address,twm.meter_num,twm.line_num,twm.billing_cycle,date_format(tad.write_time, '%Y-%m-%d') as todays
 from t_actual_data tad  inner join t_company  tc on tad.inner_code=tc.inner_code  inner join t_water_meter twm on tad.meter_address=twm.meter_address where 1=1
and tad.write_time >= '2018-03-19 00:00:00'  and tad.write_time < '2018-03-20 00:00:00'  and tad.meter_address = '1709100003'
group by tad.meter_address,date_format(tad.write_time, '%Y-%m-%d')  order by date_format(tad.write_time, '%Y-%m-%d') desc,tad.meter_address desc ;

 select tc.inner_code,tc.name,tc.address,tc.water_unit,tc.county,tc.company_type,twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address,
date_format(tad.write_time, '%Y-%m') as months,sum(tad.net_water) as monthTotal
 from t_actual_data tad  inner join t_company  tc on tad.inner_code=tc.inner_code  inner join t_water_meter twm on tad.meter_address=twm.meter_address  where 1=1
 group by tad.meter_address,date_format(tad.write_time, '%Y-%m') order by months desc,tad.meter_address desc;



select *,sum(net_water) from t_actual_data where 1=1 and write_time >= '2018-01-01 00:00:00' and write_time < '2018-04-01 00:00:00' and meter_address='201707000000914'




select tad.*,
		tc.name,tc.address,tc.water_unit,tc.county,tc.company_type,
				twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,
				date_format(tad.write_time, '%Y-%m') as months,sum(tad.net_water) as monthTotal

		from t_actual_data tad
		inner join t_company  tc on tad.inner_code=tc.inner_code
		inner join t_water_meter twm on tad.meter_address=twm.meter_address
		where 1=1

		and tc.name like '%潞洲水务有限公司%'
		and tad.write_time >= '2018-02-01 00:00:00' and tad.write_time < '2019-01-01 00:00:00' and tad.meter_address = '201707000000936'
		group by tad.inner_code,date_format(tad.write_time, '%Y-%m')
		order by months desc,tad.inner_code desc



select * from (select allad.*,sum(allad.net_water) as sumWater from (select tad.*,twm.waters_type from t_actual_data tad
inner join (select waters_type,meter_address from t_water_meter) twm on twm.meter_address=tad.meter_address) allad

where allad.write_time >='2018-03-01 00:00:00'
and allad.write_time <'2018-04-30 23:59:59' group by allad.meter_address) t

INNER join (select march,april,inner_code,waters_type from t_water_index) twi on twi.inner_code=t.inner_code
where t.sumWater>(IFNULL(twi.march,0) + IFNULL(twi.april,0)) and t.waters_type=twi.waters_type



select * from (select allad.*,sum(allad.net_water) as sumWater, from (select tad.inner_code,tad.net_water,tad.meter_address,tad.write_time,twm.waters_type from t_actual_data tad
inner join (select waters_type,meter_address from t_water_meter) twm on twm.meter_address=tad.meter_address) allad
where allad.write_time >='2018-03-01 00:00:00' and allad.write_time <'2018-03-02 23:59:59' group by allad.inner_code) t

inner join (select march,april,inner_code,waters_type from t_water_index) twi on twi.inner_code=t.inner_code
inner join (select name,inner_code,address,water_unit,county,company_type from t_company) tc on tc.inner_code=t.inner_code
where  t.waters_type=twi.waters_type


select sum(net_water) as sumWater from t_actual_data  where write_time >='2018-03-01 00:00:00' and write_time <'2018-03-02 23:59:59' and inner_code='100002'