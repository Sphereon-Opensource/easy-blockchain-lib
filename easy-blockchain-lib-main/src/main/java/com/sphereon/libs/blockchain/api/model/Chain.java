/*
 * Copyright (c) 2017 Sphereon B.V. <https://sphereon.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sphereon.libs.blockchain.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by niels on 18-10-16.
 */
@XmlRootElement
@ApiModel(value = "Chain", description = "This is the Chain object itself. A chain is created with a first Entry. The chain Id is calculated from the firt Entry where the content part of the entry is omited during Chain ID calculation. After the chain is stored you can traverse the cahin using the API (not this object)")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Chain {

    public Chain() {
    }

    public Chain(final Entry firstEntry) {
        this.firstEntry = firstEntry;
    }

    @XmlElement(name = "firstEntry", required = true)
    @ApiModelProperty(notes = "The First Entry with which this Chain was created. Traverse the chain using the API (not this object)", required = true)
    private Entry firstEntry;


    public Entry getFirstEntry() {
        return firstEntry;
    }


    public void setFirstEntry(Entry firstEntry) {
        this.firstEntry = firstEntry;
    }


    @Override
    public String toString() {
        return "Chain{" +
                "firstEntry=" + firstEntry +
                '}';
    }
}
