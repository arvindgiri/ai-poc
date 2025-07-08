# AI POCs (From Beginner to Intermediate)

Explore a progression of Retrieval-Augmented Generation (RAG) based project ideas using LangChain, FAISS, Chroma, and more — from simple chatbots to intermediate-level knowledge assistants.

---

## 1. POC - Building a very basic LM  
**Complexity**: Beginner  

**Description**:  
Create a basic example LM which can answer questions from a paragraph

**Tools/Add-ons**:  
- Python
- 

---

## 2. Chat with Your PDF / TXT / CSV  
**Complexity**: Beginner  

**Description**:  
Upload a document and ask questions about it. Uses simple FAISS + LangChain RetrievalQA.

**Add-ons**:  
- Support for multiple formats (PDF, TXT, CSV)  
- Chunking and embedding

---

## 3. Multi-File Document Q&A  
**Complexity**: Intermediate  

**Description**:  
Ingest multiple documents and store embeddings in a persistent DB like Chroma or Pinecone. Enables querying across all files.

**Add-ons**:  
- Metadata filtering (e.g., file name, date)  
- Multi-document context

---

## 4. Chat with GitHub Repo  
**Complexity**: Intermediate  

**Description**:  
Clone a GitHub repo, extract code, README, and docstrings, then build a vector index for Q&A like:  
_“What does this repo do?”_ or _“How is authentication handled?”_

**Add-ons**:  
- Parsing code using `GitLoader` or `TextLoader` + `RegexSplitter`

---

## 5. Multi-Tool Agent with RAG  
**Complexity**: Intermediate+  

**Description**:  
RAG-powered assistant that also uses LangChain agents for tools (e.g., calculator, web search, DB query).  
Use case: _“Summarize the document, get live stock price, and compare them.”_

**Add-ons**:  
- Tool integration  
- Dynamic agent workflows

---

## 6. Hybrid RAG: Structured + Unstructured Data  
**Complexity**: Intermediate+  

**Description**:  
Combine SQL/API structured data with unstructured documents.  
Example: _“List customers from CRM who mentioned ‘refund’ in support tickets.”_

**Add-ons**:  
- Custom retriever for SQL  
- Hybrid query engine

---

## 7. Domain-Specific RAG Bot with Feedback Loop  
**Complexity**: Intermediate+  

**Description**:  
Enterprise bot for domain-specific Q&A (e.g., legal, healthcare). Incorporates feedback to fine-tune models or embeddings.

**Add-ons**:  
- Feedback collection and ranking  
- Human-in-the-loop review  
- Dashboards and logging
