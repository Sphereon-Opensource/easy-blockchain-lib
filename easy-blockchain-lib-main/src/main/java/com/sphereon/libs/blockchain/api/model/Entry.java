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
@ApiModel(description = "Entry")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Entry {
    @XmlElement(name = "entryData", required = true)
    @ApiModelProperty(value = "The Entry Data", notes = "The actual Entry Data. Delegation was choosen in order for this class to be easily extendable in the future.", required = true)
    private EntryData entryData;


    public EntryData getEntryData() {
        return entryData;
    }

    public void setEntryData(EntryData entryData) {
        this.entryData = entryData;
    }


    @Override
    public String toString() {
        return "Entry{" +
                "entryData=" + entryData +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        return getEntryData() != null ? getEntryData().equals(entry.getEntryData()) : entry.getEntryData() == null;
    }

    @Override
    public int hashCode() {
        return getEntryData() != null ? getEntryData().hashCode() : 0;
    }
}
