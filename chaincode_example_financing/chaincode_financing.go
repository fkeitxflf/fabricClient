package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

var financingNo int = 0
var bankNo int = 0
var cpNo int = 0
var transactionNo int = 0

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

type Financing struct {
	ID			int
	CoreCompanyID        string
	FinanceCompanyID string
	BankID  string
	Amount 	int
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}

// Init the block chain
func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting 1")
	}
	startBytes, err := json.Marshal(args[0])
	err = stub.PutState("StartBlock", startBytes)
	if err != nil {
		return nil, errors.New("PutState Error" + err.Error())
	}
	return startBytes, nil
}

// Invoke isur entry point to invoke a chaincode function
func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if function == "createFinancing" {
		return t.createFinancing(stub, args)
	} 
//	else if function == "createCompany" {
//		return t.createCompany(stub, args)
//	} else if function == "issueCoin" {
//		return t.issueCoin(stub, args)
//	} else if function == "issueCoinToBank" {
//		return t.issueCoinToBank(stub, args)
//	} else if function == "issueCoinToCp" {
//		return t.issueCoinToCp(stub, args)
//	} else if function =="transfer"{
//		return t.transfer(stub,args)
//	}

	return nil, errors.New("Received unknown function invocation")
}

// Create the finacing request
func (t *SimpleChaincode) createFinancing(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 4 {
		return nil, errors.New("Incorrect number of arguments. Expecting 4")
	}
	var financing Financing
	var financingBytes []byte
	var amountNumber int
	amountNumber,err := strconv.Atoi(args[3])
		if err != nil {
		return nil, errors.New("Expecting integer value for asset holding")
	}
	financing = Financing{ID:financingNo, CoreCompanyID:args[0],FinanceCompanyID:args[1],BankID:args[2],Amount:amountNumber}

	err = writeFinancing(stub,financing)
	if err != nil {
		return nil, errors.New("write Error" + err.Error())
	}

	financingBytes,err = json.Marshal(&financing)
	if err!= nil{
		return nil,errors.New("Error retrieving cbBytes")
	}

	financingNo = financingNo +1
	return financingBytes, nil
}

// Write the financing request to the block chain
func writeFinancing(stub shim.ChaincodeStubInterface,financing Financing) (error) {
	var financingId string
	financingBytes, err := json.Marshal(&financing)
	if err != nil {
		return err
	}
	financingId= strconv.Itoa(financing.ID)
	if err!= nil{
		return errors.New("want Integer number")
	}
	err = stub.PutState("financing"+financingId, financingBytes)
	if err != nil {
		return errors.New("PutState Error" + err.Error())
	}
	return nil
}

// Query is our entry point for queries
func (t *SimpleChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println("query is running " + function)

	return nil,nil
}


