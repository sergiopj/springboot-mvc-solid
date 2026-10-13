package com.atlas.bank.atlas_bank.transaction.service.transfer;

import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;

public interface ITransferService {

    Transaction execute(TransferRequest request);

}
