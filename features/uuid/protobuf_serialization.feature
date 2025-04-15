#
# Copyright (c) 2025 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0
#
# SPDX-FileType: SOURCE
# SPDX-License-Identifier: Apache-2.0
#
Feature: Efficient binary encoding of uProtocol UUIDs

  Scenario Outline:
    Developers using a uProtocol language library should be able to get the binary
    encoding of a uProtocol UUID instance as specified by the UUID proto3 definition file.

    [utest->req~uuid-proto~1]

    The byte sequences representing the Protocol Buffer encodings have been created
    using https://www.protobufpal.com/ based on the UUID proto3 definition from the
    uProtocol specification.

    Given a UUID having MSB <uuid_msb> and LSB <uuid_lsb>
    When serializing the UUID to its protobuf wire format
    Then the resulting byte sequence is <protobuf>
    And the original UUID can be recreated from the protobuf wire format

    Examples:
      | uuid_msb           | uuid_lsb           | protobuf                             |
      | 0x0000000000017000 | 0x8010101010101a1a | 090070010000000000111a1a101010101080 |
