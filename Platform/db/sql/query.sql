

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