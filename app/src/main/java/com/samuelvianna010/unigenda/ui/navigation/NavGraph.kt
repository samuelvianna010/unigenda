package com.samuelvianna010.unigenda.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.screens.addAssessment.AddAssessmentScreen
import com.samuelvianna010.unigenda.screens.assessmentDetails.AssessmentDetailsScreen
import com.samuelvianna010.unigenda.screens.subjectDetails.SubjectDetailsScreen
import com.samuelvianna010.unigenda.screens.addSubject.AddSubjectScreen
import com.samuelvianna010.unigenda.screens.editOrDeleteSubject.EditOrDeleteSubjectScreen
import com.samuelvianna010.unigenda.screens.editOrDeleteAssessment.EditOrDeleteAssessmentScreen
import com.samuelvianna010.unigenda.screens.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onViewSubjectDetails = { subjectId ->
                    navController.navigate(Screen.SubjectDetails(subjectId))
                },
                onViewAssessmentDetails = { assessmentId ->
                    navController.navigate(Screen.AssessmentDetails(assessmentId))
                },
                onAddSubject = {
                    navController.navigate(Screen.AddSubject)
                },
                onAddAssessment = {
                    navController.navigate(Screen.AddAssessment)
                },
                subjectViewModel = hiltViewModel(),
                assessmentsViewModel = hiltViewModel()
            )
        }

        composable<Screen.AddSubject> {
            AddSubjectScreen(
                onBack = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        composable<Screen.AddAssessment> {
            AddAssessmentScreen(
                onBack = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        composable<Screen.SubjectDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.SubjectDetails>()
            SubjectDetailsScreen(
                subjectId = args.subjectId,
                subjectViewModel = hiltViewModel(),
                assessmentsViewModel = hiltViewModel(),
                onEditClick = { id ->
                    navController.navigate(Screen.EditOrDeleteSubject(id))
                },
                onAssessmentClick = { id ->
                    navController.navigate(Screen.AssessmentDetails(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Screen.AssessmentDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AssessmentDetails>()
            AssessmentDetailsScreen(
                assessmentId = args.assessmentId,
                assessmentsViewModel = hiltViewModel(),
                subjectViewModel = hiltViewModel(),
                onEditClick = { id ->
                    navController.navigate(Screen.EditOrDeleteAssessment(id))
                },
                onBack = { navController.popBackStack()

				}
            )
        }

        composable<Screen.EditOrDeleteSubject> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditOrDeleteSubject>()
            EditOrDeleteSubjectScreen(
                subjectId = args.subjectId,
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onDelete = {
                    navController.popBackStack(Screen.Home, inclusive = false)
                }
            )
        }

        composable<Screen.EditOrDeleteAssessment> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditOrDeleteAssessment>()
            EditOrDeleteAssessmentScreen(
                taskId = args.assessmentId,
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onDelete = {
                    navController.popBackStack(Screen.Home, inclusive = false)
                }
            )
        }
    }
}
