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
package io.mifos.deposit.service.internal.service.helper;

import io.mifos.accounting.api.v1.client.AccountNotFoundException;
import io.mifos.accounting.api.v1.client.LedgerManager;
import io.mifos.accounting.api.v1.client.LedgerNotFoundException;
import io.mifos.accounting.api.v1.domain.Account;
import io.mifos.accounting.api.v1.domain.Ledger;
import io.mifos.core.lang.ServiceException;
import io.mifos.deposit.service.ServiceConstants;
import io.mifos.deposit.service.internal.repository.ProductDefinitionEntity;
import io.mifos.deposit.service.internal.repository.ProductInstanceEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class AccountingService {

  private final Logger logger;
  private final LedgerManager ledgerManager;

  @Autowired
  public AccountingService(@Qualifier(ServiceConstants.LOGGER_NAME) final Logger logger,
                           final LedgerManager ledgerManager) {
    super();
    this.logger = logger;
    this.ledgerManager = ledgerManager;
  }

  public void createAccount(final ProductDefinitionEntity productDefinitionEntity,
                            final ProductInstanceEntity productInstanceEntity) {
    final String ledgerIdentifier = productDefinitionEntity.getEquityLedgerIdentifier();
    try {
      final Ledger ledger = this.ledgerManager.findLedger(ledgerIdentifier);
      final Account account = new Account();
      account.setIdentifier(productInstanceEntity.getAccountIdentifier());
      account.setType(ledger.getType());
      account.setLedger(ledgerIdentifier);
      account.setName(productDefinitionEntity.getName());

      account.setHolders(new HashSet<>(
          Arrays.asList(productInstanceEntity.getCustomerIdentifier()))
      );

      if (productInstanceEntity.getBeneficiaries() != null) {
        account.setSignatureAuthorities(new HashSet<>(
            Arrays.asList(StringUtils.split(productInstanceEntity.getBeneficiaries(), ","))
        ));
      }

      account.setBalance(0.00D);
      this.ledgerManager.createAccount(account);
    } catch (final LedgerNotFoundException lnfex) {
      throw ServiceException.notFound("Ledger {0} not found.", ledgerIdentifier);
    }
  }

  public Account findAccount(final String identifier) {
    try {
      return this.ledgerManager.findAccount(identifier);
    } catch (final AccountNotFoundException anfex) {
      throw ServiceException.notFound("Account {0} not found.", identifier);
    }
  }

  public void updateAccount(final Account account) {
    this.ledgerManager.modifyAccount(account.getIdentifier(), account);
  }
}
