/*
 * Copyright 2017 The Mifos Initiative.
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
package io.mifos.deposit;

import io.mifos.deposit.api.v1.definition.domain.Action;
import io.mifos.deposit.api.v1.definition.domain.Charge;
import io.mifos.deposit.api.v1.definition.domain.Currency;
import io.mifos.deposit.api.v1.definition.domain.ProductDefinition;
import io.mifos.deposit.api.v1.definition.domain.Term;
import io.mifos.deposit.api.v1.domain.InterestPayable;
import io.mifos.deposit.api.v1.domain.TimeUnit;
import io.mifos.deposit.api.v1.domain.Type;
import io.mifos.deposit.api.v1.instance.domain.ProductInstance;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.HashSet;

public class Fixture {

  public Fixture() {
    super();
  }

  static ProductDefinition productDefinition() {
    final Charge openingCharge = new Charge();
    openingCharge.setActionIdentifier("Open");
    openingCharge.setAmount(2.00D);
    openingCharge.setName("Opening Account Charge");
    openingCharge.setIncomeAccountIdentifier("10123");
    openingCharge.setProportional(Boolean.FALSE);

    final Charge closingCharge = new Charge();
    closingCharge.setActionIdentifier("Close");
    closingCharge.setAmount(2.00D);
    closingCharge.setName("Closing Account Fee");
    closingCharge.setIncomeAccountIdentifier("10123");
    closingCharge.setProportional(Boolean.FALSE);

    final Term term = new Term();
    term.setPeriod(12);
    term.setTimeUnit(TimeUnit.MONTH.name());
    term.setInterestPayable(InterestPayable.MATURITY.name());

    final Currency currency = new Currency();
    currency.setCode("USD");
    currency.setName("US Dollar");
    currency.setSign("$");
    currency.setScale(3);

    final ProductDefinition productDefinition = new ProductDefinition();
    productDefinition.setType(Type.SHARE.name());
    productDefinition.setIdentifier(RandomStringUtils.randomAlphanumeric(8));
    productDefinition.setName(RandomStringUtils.randomAlphanumeric(256));
    productDefinition.setDescription(RandomStringUtils.randomAlphanumeric(2048));
    productDefinition.setCharges(new HashSet<>(Arrays.asList(openingCharge, closingCharge)));
    productDefinition.setCurrency(currency);
    productDefinition.setInterest(1.25D);
    productDefinition.setEquityLedgerIdentifier("20300");
    productDefinition.setExpenseAccountIdentifier("30300");
    productDefinition.setFlexible(Boolean.FALSE);
    productDefinition.setMinimumBalance(50.00);
    productDefinition.setTerm(term);

    return productDefinition;
  }

  public static Action action() {
    final Action action = new Action();
    action.setIdentifier(RandomStringUtils.randomAlphanumeric(32));
    action.setName(RandomStringUtils.randomAlphanumeric(256));
    action.setDescription(RandomStringUtils.randomAlphanumeric(2048));
    action.setTransactionType("ACCO");

    return action;
  }

  public static ProductInstance productInstance(final String productIdentifier) {
    final ProductInstance productInstance = new ProductInstance();
    productInstance.setProductIdentifier(productIdentifier);
    productInstance.setCustomerIdentifier(RandomStringUtils.randomAlphanumeric(8));
    productInstance.setBeneficiaries(new HashSet<>(Arrays.asList(
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8)
    )));
    return productInstance;
  }
}
