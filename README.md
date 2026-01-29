# Introdução

Este relatório visa apresentar a arquitetura e o processo de desenvolvimento do Trabalho Prático da unidade curricular de Programação de Dispositivos Móveis, realizado pelo aluno Rodrigo Castro.

A ideia da BookBox surgiu da minha admiração pela aplicação **Letterboxd** e do meu interesse pessoal pela leitura. A Letterboxd permite aos utilizadores criar e partilhar reviews de filmes, promovendo uma comunidade ativa em torno do cinema. Inspirado nesse conceito, procurei desenvolver um projeto semelhante, adaptado ao universo literário e numa escala mais reduzida.

A BookBox é uma aplicação móvel dedicada à gestão de bibliotecas pessoais e à monitorização de hábitos de leitura, permitindo aos seus utilizadores encontrar novos livros, acompanhar o seu progresso de leitura e registar as suas reviews.


# Estrutura do Projeto

```estrutura
ipca.example.bookbox/
├── api/                        
│   ├── data/                   
│   ├── APIModule.kt            
│   └── BookApiService.kt       
├── models/                     
│   ├── book/                  
│   ├── progress/               
│   ├── review/                 
│   ├── user/                  
│   ├── wishlistitem/          
│   ├── AppDatabase.kt         
│   └── DataModule.kt          
├── repository/                 
│   ├── AuthenticationRepository.kt
│   ├── BookRepository.kt
│   ├── ProgressRepository.kt
│   └── ResultWrapper.kt       
├── ui/                         
│   ├── addBook/                
│   ├── Authentication/         
│   ├── components/             
│   ├── Homepage/               
│   ├── Profile/               
│   ├── Progress/             
│   └── theme/                  
├── BookboxApp.kt               
├── FirebaseModule.kt          
└── MainActivity.kt             
```

api/ -  Responsável pela comunicação com a Google Books API, contendo as configurações do Retrofit e definições de endpoints.

models/ - Define as entidades da Data Access Layer e a estrutura da database local.

repository/ - Atua como a "Single Source of Truth", mediando o flow de dados entre a API, a Firebase e a database local.

ui/ - UI da Aplicação, Jetpack Compose.

.Module - Responsável pela implementação da injeção de dependências através do Hilt, garantindo o desacoplamento entre componentes e facilitando a testabilidade da aplicação.


# Funcionalidades

A aplicação oferece um conjunto vasto de ferramentas para o utilizador usufruir:

- **Autenticação**: Registo e Login (via Email ou Username) integrados com a Firebase Auth.

- **Catálogo de livros**: Pesquisa em tempo real de livros através de integração com a Google Books API.

- **Biblioteca Pessoal**: Criação, edição e remoção de livros inseridos manualmente pelo utilizador.

- **Reading Progress**: Acompanhamento do progresso de leitura associado a cada livro, incluindo um espaço destinado ao registo de observações pessoais.

- **Wishlist & Reviews**: Funcionalidade que permite adicionar livros a uma wishlist e registar reviews relativas a livros já concluídos.


# Modelo de Dados
## Diagrama de Caso de Uso
![PDMUC](https://github.com/user-attachments/assets/50be3747-366d-4d32-b65d-a2f0bf008031)


## Diagrama de Entidade-Relação
![PDMer](https://github.com/user-attachments/assets/9b763e54-d4d3-4e26-bf6e-d28bc85f1a63)


# Mockups

<img width="412" height="687" alt="Screenshot 2026-01-29 at 18 49 04" src="https://github.com/user-attachments/assets/2a03bba4-912d-4f3b-9e5a-f4b9785c7281" />
<img width="803" height="687" alt="Screenshot 2026-01-29 at 18 48 51" src="https://github.com/user-attachments/assets/44b74237-da5f-4360-8b1b-51d726c7e291" />
<img width="1051" height="701" alt="Screenshot 2026-01-29 at 18 48 38" src="https://github.com/user-attachments/assets/dadb0c34-a7ab-4309-8627-2ec84d836eb6" />
<img width="447" height="701" alt="Screenshot 2026-01-29 at 18 48 24" src="https://github.com/user-attachments/assets/c67d31b2-3fb8-48cc-9354-38ef513c40d4" />
<img width="854" height="701" alt="Screenshot 2026-01-29 at 18 48 11" src="https://github.com/user-attachments/assets/714ed5f3-7b23-47d2-9849-049e9c9f07a8" />
<img width="1191" height="701" alt="Screenshot 2026-01-29 at 18 47 59" src="https://github.com/user-attachments/assets/fa0b3e63-32a5-416a-a10b-b6032284dd96" />
<img width="712" height="701" alt="Screenshot 2026-01-29 at 18 47 33" src="https://github.com/user-attachments/assets/9bd8e199-0ab8-4f55-b685-a3a7d21e37b2" />


# Tecnologias
A aplicação foi construída seguindo as práticas lecionadas durante a disciplina:

- Linguagem: Kotlin.

- Interface: Desenvolvida inteiramente em Jetpack Compose.

- Arquitetura: Implementação do padrão MVVM  para garantir a separação de responsabilidades e facilidade de manutenção.

- Backend: Utilização da Firebase para autenticação de utilizadores, e Cloud Firestore para o armazenamento de dados em tempo real.

- Injeção de Dependências: Implementação do Hilt para gerir o lifecycle dos componentes e facilitar o acesso aos repositórios de dados.

# Dificuldades

1. Implementação do Padrão MVVM
- A principal dificuldade consistiu em garantir uma separação clara entre a lógica de dados e a interface. A gestão de estados reativos no Jetpack Compose exigiu especial atenção para assegurar que a interface refletia corretamente o    estado do ViewModel sem perda de informação durante as recomposições.
  
2. Navegação e Passagem de Dados
- A navegação entre ecrãs revelou-se desafiante, sobretudo na passagem de múltiplos argumentos. Foi necessário recorrer à codificação de URLs e à configuração de navArguments para garantir a transmissão correta de dados entre páginas, evitando chamadas desnecessárias à API.
  
3. Estabilidade da Aplicação
- A integração com o Firebase e uma API externa trouxe desafios relacionados com a consistência dos dados. A implementação de validações adicionais foi essencial para prevenir falhas da aplicação e garantir uma experiência de utilização estável.

# Conclusão

O desenvolvimento da BookBox permitiu-me consolidar os conhecimentos lecionados na disciplina, ao mesmo tempo que despertou um maior interesse por esta área da programação. 
A transição para uma interface totalmente declarativa com Jetpack Compose, aliada à utilização de um backend em tempo real como o Firebase, resultou numa aplicação robusta que cumpre os objetivos propostos de interoperabilidade e portabilidade.


