9# Unigenda 🎓

Unigenda é um organizador acadêmico pessoal desenvolvido para estudantes que desejam manter o controle total de sua vida universitária. Com uma interface moderna e intuitiva, o aplicativo permite gerenciar disciplinas, avaliações, notas e frequência em um só lugar.

## ✨ Funcionalidades

-   📚 **Gestão de Disciplinas**: Cadastre suas matérias, professores e períodos letivos.
-   🗓️ **Cronograma Semanal**: Defina os dias de aula e visualize sua agenda.
-   📝 **Controle de Avaliações**: Organize provas, trabalhos e projetos com diferentes níveis de urgência e pesos.
-   📊 **Cálculo de Notas**: Acompanhe seu desempenho acadêmico com cálculos automáticos de média baseados em pesos.
-   ✅ **Registro de Presença**: Controle sua frequência aula a aula para evitar reprovações por falta.
-   🎨 **Personalização Dinâmica**: Cada disciplina possui um esquema de cores único que se reflete em toda a interface do app.
-   🌗 **Suporte a Modo Escuro**: Conforto visual em qualquer ambiente.

## 📸 Previews

Aqui estão algumas capturas de tela das principais funcionalidades do Unigenda:

|                     Tela Inicial                      |               Detalhes da Avaliação               |
|:-----------------------------------------------------:|:-------------------------------------------------:|
|         ![HomeScreen](/previews/preview1.jpg)         |   ![AssessmentDetails](/previews/preview2.jpg)    |
| Visualize suas próximas tarefas e disciplinas ativas. | Gerencie notas, pesos e prazos de cada atividade. |

|               Detalhes da Disciplina                |                Controle de Frequência                 |
|:---------------------------------------------------:|:-----------------------------------------------------:|
|      ![SubjectDetails](/previews/preview3.jpg)      |         ![Attendance](/previews/preview4.jpg)         |
| Visão geral do progresso em uma matéria específica. | Acompanhe suas presenças e faltas de forma detalhada. |

## 🛠️ Tecnologias Utilizadas

O projeto foi construído utilizando as ferramentas e bibliotecas mais modernas do ecossistema Android:

-   **Linguagem:** [Kotlin](https://kotlinlang.org/)
-   **Interface UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
-   **Arquitetura:** MVVM (Model-View-ViewModel) com Clean Architecture principles.
-   **Injeção de Dependência:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
-   **Banco de Dados Local:** [Room Database](https://developer.android.com/training/data-storage/room)
-   **Navegação:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
-   **Gerenciamento de Estado:** Flow e StateFlow do Kotlin Coroutines.

## 🚀 Como Executar o Projeto

1.  Clone este repositório:
    ```bash
    git clone https://github.com/samuelvianna010/Unigenda.git
    ```
2.  Abra o projeto no **Android Studio** (versão Ladybug ou superior recomendada).
3.  Sincronize o Gradle.
4.  Execute o aplicativo em um emulador ou dispositivo físico com Android 8.0 (API 26) ou superior.

## 📂 Estrutura do Projeto

-   `app/src/main/java/com/samuelvianna010/unigenda/`:
    -   `components/`: Componentes de UI reutilizáveis (cards, seletores, campos de data).
    -   `database/`: Entidades do Room (Subject, Assessment, Lecture), DAOs e ViewModels.
    -   `screens/`: Telas principais do aplicativo (Home, AddSubject, SubjectDetails, etc.).
    -   `ui/`: Definições de tema (Cores, Tipografia) e lógica de navegação.
    -   `core/`: Utilitários e classes base.

---
Desenvolvido por [Samuel Vianna](https://github.com/samuelvianna010) 🚀
