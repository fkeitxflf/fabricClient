package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

var financingNo int = 0
var bankNo int = 0
var cpNo int = 0
var transactionNo int = 0

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

// 
type Financing struct {
	ID					int
	CoreCompanyID       string
	FinanceCompanyID 	string
	BankID  			string
	Amount 				int
}

type Company struct {
	CompanyID   		string
	Type				string  // 0: CoreCompay, 1:loanCompany
	Amount 				map[string] int
}

type Transaction struct {
	FromID   string
	ToID     string
	Time     int64
	Amount   int
	Note	 string
	ID       int
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
	} else if function == "createCompany" {
		return t.createCompany(stub, args)
	} else if function == "createCoinToCoreCp" {
		return t.createCoinToCoreCp(stub, args)
	} else if function == "transferCoin" {
		return t.transferCoin(stub, args)
	}

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
	financing = Financing{ID:financingNo, CoreCompanyID:args[0], FinanceCompanyID:args[1],BankID:args[2],Amount:amountNumber}

	err = writeFinancing(stub,financing)
	if err != nil {
		return nil, errors.New("write Error" + err.Error())
	}

	financingBytes,err = json.Marshal(&financing)
	if err!= nil{
		return nil,errors.New("Error retrieving cbBytes")
	}

	financingNo = financingNo +1
	
	fmt.Println(string(financingBytes))
	return financingBytes,nil
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

// Create company, the type=0 is the core company the type=1 is the loan company
func (t *SimpleChaincode) createCompany(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 4 {
		return nil, errors.New("Incorrect number of arguments. Expecting 4")
	}
	var company Company
	mapAmount := make(map[string] int)
	amountNumber,err := strconv.Atoi(args[3])
	mapAmount[args[2]] = amountNumber
	if err != nil {
		return nil, errors.New("Expecting integer value for asset holding")
	}
	
	company = Company{CompanyID:args[0],Type:args[1],Amount:mapAmount}

	err = writeCompany(stub,company)
	if err != nil{
		return nil, errors.New("write Error" + err.Error())
	}

	cpBytes,err := json.Marshal(&company)
	if(err!=nil){
		return nil,err
	}

	cpNo = cpNo +1
	return cpBytes, nil
}

func writeCompany(stub shim.ChaincodeStubInterface,company Company) (error) {
	cpBytes, err := json.Marshal(&company)
	if err != nil {
		return err
	}
	err = stub.PutState(company.CompanyID, cpBytes)
	if err != nil {
		return errors.New("PutState Error" + err.Error())
	}
	return nil
}

func (t *SimpleChaincode) createCoinToCoreCp(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 3 {
		return nil, errors.New("Incorrect number of arguments. Expecting 3")
	}

	var company Company
	var companyId string
	var coinNumber int
	var cpBytes []byte
	var err error

	coinNumber,err= strconv.Atoi(args[1])
	if err!=nil{
		return nil,errors.New("want integer")
	}
	
	companyId = args[0]
	company,_,err = getCompanyById(stub,companyId)
	if err != nil {
		return nil,errors.New("get errors")
	}
	
	mapAmount := company.Amount
	for key, value := range mapAmount {
		if strings.EqualFold(key, args[2]) {
			 mapAmount[args[2]] = value + coinNumber
			 company.Amount = mapAmount
		} else {
			mapAmount[args[2]] = coinNumber
			company.Amount = mapAmount
		}
	}

	err = writeCompany(stub,company)
	if err != nil {
		return nil, errors.New("Write company errors"+err.Error())
	}

	cpBytes,err = json.Marshal(&company)
	if err != nil {
		fmt.Println("Error unmarshalling centerBank")
	}

	return cpBytes, nil
}

func getCompanyById(stub shim.ChaincodeStubInterface, id string) (Company,[]byte, error) {
	var company Company
	cpBytes,err := stub.GetState(id)
	if err != nil {
		fmt.Println("Error retrieving cpBytes")
	}
	err = json.Unmarshal(cpBytes, &company)
	if err != nil {
		fmt.Println("Error unmarshalling centerBank")
	}
	return company,cpBytes, nil
}

func (t *SimpleChaincode) transferCoin(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 5 {
		return nil, errors.New("Incorrect number of arguments. Expecting 5")
	}

	var companyFrom Company
	var companyTo Company
	var companyFromId string
	var companyToId string
	var bankId string
	var note string
	var amount int
	var tsBytes [] byte
	var err error

	companyFromId = args[0]
	companyToId = args[1]
	bankId = args[2]
	amount,err = strconv.Atoi(args[3])
	if err != nil {
		return nil, errors.New("Expecting integer value for asset holding")
	}
	note = args[4]

	companyFrom,_,err = getCompanyById(stub,companyFromId)
	if err != nil {
		return nil,errors.New("get errors when get companyFrom")
	}

	companyTo,_,err = getCompanyById(stub,companyToId)
	if err != nil {
		return nil,errors.New("get errors when get companyTo")
	}

	valueFrom, ok := companyFrom.Amount[bankId]
	if ok {
	 	valueTo, ok := companyTo.Amount[bankId]
	 	if ok {
	 		companyTo.Amount[bankId] = valueTo + amount
	 		companyFrom.Amount[bankId] = valueFrom - amount
	 	} else {
	 		return nil,errors.New("No Bank info for the toCompany")
	 	}
	 } else{
		 return nil,errors.New("No Bank info for the FromCompany")
	 }

	err = writeCompany(stub,companyFrom)
	if err != nil {
		return nil, errors.New("write company error" + err.Error())
	}

	err = writeCompany(stub,companyTo)
	if err != nil {
		// TODO: Need rollover
		return nil, errors.New("write company Error" + err.Error())
	}

	transaction := Transaction{FromID:companyFromId,ToID:companyToId,Time:time.Now().Unix(),Amount:amount,Note:note, ID:transactionNo}
	err = writeTransaction(stub,transaction)
	if err != nil {
		return nil, errors.New("write Error" + err.Error())
	}

	tsBytes,err = json.Marshal(&transaction)
	if err != nil {
		fmt.Println("Error unmarshalling centerBank")
	}

	transactionNo = transactionNo +1
	return tsBytes, nil
}

func writeTransaction(stub shim.ChaincodeStubInterface,transaction Transaction) (error) {
	var tsId string
	tsBytes, err := json.Marshal(&transaction)
	if err != nil {
		return err
	}
	tsId= strconv.Itoa(transaction.ID)
	if err!= nil{
		return errors.New("want Integer number")
	}
	err = stub.PutState("transaction"+tsId, tsBytes)
	if err != nil {
		return errors.New("PutState Error" + err.Error())
	}
	return nil
}

func getTransactionById(stub shim.ChaincodeStubInterface, id string) (Transaction,[]byte, error) {
	var transaction Transaction
	tsBytes,err := stub.GetState("transaction"+id)
	if err != nil {
		fmt.Println("Error retrieving cpBytes")
	}
	err = json.Unmarshal(tsBytes, &transaction)
	if err != nil {
		fmt.Println("Error unmarshalling centerBank")
	}
	return transaction,tsBytes, nil
}


func (t *SimpleChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println("query is running " + function)

	if function == "getCompanyById" {
		if len(args) != 1 {
			return nil, errors.New("Incorrect number of arguments. Expecting 1")
		}
		_,cpBytes, err := getCompanyById(stub, args[0])
		if err != nil {
			fmt.Println("Error unmarshalling centerBank")
			return nil, err
		}
		return cpBytes, nil
	} else if function == "getTransactionById" {
		if len(args) != 1 {
			return nil, errors.New("Incorrect number of arguments. Expecting 1")
		}
		_,tsBytes, err := getTransactionById(stub, args[0])
		if err != nil {
			fmt.Println("Error unmarshalling")
			return nil, err
		}
		return tsBytes, nil
	} 
	return nil,nil
}


