/*
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.kubernetes.client.internal;

import io.fabric8.kubernetes.api.model.AuthInfo;
import io.fabric8.kubernetes.api.model.Cluster;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.ConfigBuilder;
import io.fabric8.kubernetes.api.model.Context;
import io.fabric8.kubernetes.api.model.NamedAuthInfo;
import io.fabric8.kubernetes.api.model.NamedContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Named.named;

class KubeConfigUtilsTest {
  @Test
  void testGetNamedUserIndexFromConfig() {
    // Given
    Config config = getTestKubeConfig();

    // When
    int index = KubeConfigUtils.getNamedUserIndexFromConfig(config, "test/test-cluster:443");

    // Then
    assertEquals(2, index);
  }

  @Test
  void testGetCurrentContext() {
    // Given
    Config config = getTestKubeConfig();

    // When
    NamedContext namedContext = KubeConfigUtils.getCurrentContext(config);

    // Then
    assertNotNull(namedContext);
    assertEquals("test-context", namedContext.getName());
    assertEquals("ns1", namedContext.getContext().getNamespace());
    assertEquals("system:admin/api-testing:6334", namedContext.getContext().getUser());
    assertEquals("api-testing:6334", namedContext.getContext().getCluster());
  }

  @Test
  void testParseConfig() throws IOException {
    // Given
    File configFile = new File(getClass().getResource("/test-kubeconfig").getPath());

    // When
    Config config = KubeConfigUtils.parseConfig(configFile);

    // Then
    assertNotNull(config);
    assertEquals(1, config.getClusters().size());
    assertEquals(3, config.getContexts().size());
    assertEquals(3, config.getUsers().size());
  }

  @Test
  void testGetUserToken() {
    // Given
    Config config = getTestKubeConfig();
    Context context = Objects.requireNonNull(KubeConfigUtils.getCurrentContext(config)).getContext();

    // When
    String token = KubeConfigUtils.getUserToken(config, context);

    // Then
    assertEquals("test-token-2", token);
  }

  @Test
  void testGetCluster() {
    // Given
    Config config = getTestKubeConfig();
    Context context = Objects.requireNonNull(KubeConfigUtils.getCurrentContext(config)).getContext();

    // When
    Cluster cluster = KubeConfigUtils.getCluster(config, context);

    // Then
    assertNotNull(cluster);
  }

  @Test
  void testGetUserAuthInfo() {
    // Given
    Config config = getTestKubeConfig();
    Context context = config.getContexts().get(0).getContext();

    // When
    AuthInfo authInfo = KubeConfigUtils.getUserAuthInfo(config, context);

    // Then
    assertNotNull(authInfo);
    assertEquals("test-token-2", authInfo.getToken());
  }

  @Test
  void getAuthInfo_when_authInfoExists_returnsAuthInfo() {
    // given
    Config config = getTestKubeConfig();
    // when
    NamedAuthInfo found = KubeConfigUtils.getAuthInfo(config, "test/api-test-com:443");
    // then
    assertThat(found).isNotNull();
  }

  @Test
  void getAuthInfo_when_authInfoDoesntExist_returnsNull() {
    // given
    Config config = getTestKubeConfig();
    // when
    NamedAuthInfo found = KubeConfigUtils.getAuthInfo(config, "bogus");
    // then
    assertThat(found).isNull();
  }

  @ParameterizedTest
  @MethodSource("hasAuthInfoNamed_arguments")
  void hasAuthInfoNamed(Config config, String authInfoName, Consumer<Boolean> consumer) {
    // given
    // when
    boolean hasIt = KubeConfigUtils.hasAuthInfoNamed(config, authInfoName);
    // then
    consumer.accept(hasIt);
  }

  static Stream<Arguments> hasAuthInfoNamed_arguments() {
    return Stream.of(
        // given config with authInfo, when getAuthInfoName with existing name, then should return true
        Arguments.of(
            named("given config with authInfo", getTestKubeConfig()),
            named("given existing name", "test/api-test-com:443"),
            named("then return true", (Consumer<Boolean>) (hasIt -> assertThat(hasIt).isTrue()))),
        // given config with authInfo, when getAuthInfoName with missing name, then should return false
        Arguments.of(
            named("given config with authInfo", getTestKubeConfig()),
            named("given missing authInfo name", "bogus"),
            named("then return false", (Consumer<Boolean>) (hasIt -> assertThat(hasIt).isFalse()))),
        // given config without authInfo, when getAuthInfoName with missing name, then should return false
        Arguments.of(
            named("given config without authInfo", new ConfigBuilder().build()),
            named("given missing authInfo name", "test/api-test-com:443"),
            named("then return false", (Consumer<Boolean>) (hasIt -> assertThat(hasIt).isFalse()))));
  }

  private static Config getTestKubeConfig() {
    return new ConfigBuilder()
        .withCurrentContext("test-context")
        .addNewCluster()
        .withName("api-testing:6334")
        .withNewCluster()
        .withServer("https://api-testing:6334")
        .withInsecureSkipTlsVerify(true)
        .endCluster()
        .endCluster()
        .addNewContext()
        .withName("test-context")
        .withNewContext()
        .withCluster("api-testing:6334")
        .withNamespace("ns1")
        .withUser("system:admin/api-testing:6334")
        .endContext()
        .endContext()
        .addNewContext()
        .withNewContext()
        .withCluster("minikube")
        .withUser("minikube")
        .endContext()
        .withName("minikube")
        .endContext()
        .addNewUser()
        .withName("test/api-test-com:443")
        .withNewUser()
        .withToken("token")
        .endUser()
        .endUser()
        .addNewUser()
        .withName("minikube")
        .withNewUser()
        .withClientCertificate("/home/.minikube/profiles/minikube/client.crt")
        .withClientKey("/home/.minikube/profiles/minikube/client.key")
        .endUser()
        .endUser()
        .addNewUser()
        .withName("test/test-cluster:443")
        .withNewUser()
        .withNewAuthProvider()
        .withConfig(Collections.singletonMap("id-token", "token"))
        .endAuthProvider()
        .endUser()
        .endUser()
        .addNewUser()
        .withName("system:admin/api-testing:6334")
        .withNewUser()
        .withToken("test-token-2")
        .endUser()
        .endUser()
        .build();
  }
}
