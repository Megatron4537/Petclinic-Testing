/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.features.FeatureToggle;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller used to set feature on/off
 *
 * @author Nicolas Horta-Adam
 */
@RestController
@SpringBootApplication
@Configuration
class FeatureController {

    @PostMapping("/features")
    @ResponseBody
    public void toggleFeature(@RequestBody Map<String, Object> payload) {

        if(payload.containsKey("featureA")) FeatureToggle.setFeatureA((boolean)payload.get("featureA"));
        if(payload.containsKey("featureB")) FeatureToggle.setFeatureB((boolean)payload.get("featureB"));

    }

}
