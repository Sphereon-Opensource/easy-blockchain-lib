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

import com.sphereon.libs.blockchain.api.HasValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Arrays;

/**
 * Created by niels on 16-10-16.
 */

@ApiModel(value = "External Id", description = "External Id: Allows you to store metadata. External Id bytes are included during Chain Id and Entry Id calculation")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ExternalId implements HasValue<byte[]> {
    @XmlElement
    @ApiModelProperty(value = "Metadata value", notes = "You can store any byte value in this field. The value you supply is included during both Chain Id and Entry Id calculation")
    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public ExternalId setValue(byte[] value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "ExternalId{" +
                "value=" + Arrays.toString(value) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalId)) return false;

        ExternalId that = (ExternalId) o;

        return Arrays.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getValue());
    }
}
