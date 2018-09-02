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
package com.shangsc.front.config;

import com.jfinal.config.Routes;
import com.shangsc.front.wxapp.controller.ActualController;
import com.shangsc.front.wxapp.controller.LawRecordController;
import com.shangsc.front.wxapp.controller.LoginController;
import com.shangsc.front.wxapp.controller.MsgController;

/**
 * 微信APP端Routes配置
 *
 * @author ssc
 */
public class WxApiRoutes extends Routes {

    @Override
    public void config() {
        add("/wxapp/log", LoginController.class);
        add("/wxapp/msg", MsgController.class);
        add("/wxapp/law", LawRecordController.class);
        add("/wxapp/actual", ActualController.class);
    }

}
